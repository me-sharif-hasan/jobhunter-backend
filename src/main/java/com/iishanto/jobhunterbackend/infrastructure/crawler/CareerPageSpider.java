package com.iishanto.jobhunterbackend.infrastructure.crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.ports.indexing.JobIndexEngine;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CareerPageSpider implements AdminSiteValidationDataAdapter {
    WebDriver webDriver;
    WebCrawler webCrawler;
    JobIndexingAdapter jobIndexingAdapter;
    JobsRepository jobsRepository;
    ObjectMapper objectMapper;

    @Override
    public void runScript(String url, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow) {
        webCrawler.getHtml(url);
        List<String> foundJobs=new ArrayList<>();
        executeProcessFlowRecursive(null, processFlow, new JobExtractionCallback() {
            Map<String, String> jobMappings = new HashMap<>();
            @Override
            public void onNewMappingAvailable(String key, String output) {
                System.out.println("New mapping available: " + key + " -> " + output);
                if( key == null || output == null || output.isEmpty()) {
                    System.out.println("Invalid mapping, skipping.");
                    return;
                }
                jobMappings.put(key, output);
            }

            @Override
            public void save() {
                System.out.println("Saving mappings...");
                Jobs jobs = objectMapper.convertValue(jobMappings, Jobs.class);
                jobs.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
                jobs.setIsPresentOnSite(true);
                jobs.setDescriptionIndexed(true);
                jobsRepository.save(jobs);

                jobMappings.clear();
            }
        }, null);
    }

    private void executeProcessFlowRecursive(WebElement root, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow, JobExtractionCallback callback, Map<String, String> jobMetaMappings) {
        for (SiteAttributeValidatorModel.JobExtractionPipeline pipeline : processFlow) {
            if (pipeline instanceof SiteAttributeValidatorModel.FindElements findElements) {
                String selectorXpath = findElements.getSelector();
                if (selectorXpath == null || selectorXpath.isEmpty()) {
                    System.out.println("Selector is empty, skipping FindElements operation.");
                    continue;
                }
                List<SiteAttributeValidatorModel.JobExtractionPipeline> childPipelines = findElements.getChildPipelines();
                List<WebElement> elements = webDriver.findElements(By.xpath(selectorXpath));
                if (!elements.isEmpty() && childPipelines != null && !childPipelines.isEmpty()) {
                    for (int i = 0; i < elements.size(); i++) {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                        }
                        List<WebElement> refreshedElements = webDriver.findElements(By.xpath(selectorXpath));
                        if (refreshedElements.size() <= i) {
                            System.out.println("Element at index " + i + " not found, skipping.");
                            break;
                        }
                        Map<String,String> listPageMetadata = new HashMap<>();
                        List < SiteAttributeValidatorModel.MapElementResult> metaPipeline = findElements.getMetaFieldsMapping();
                        if (metaPipeline != null && !metaPipeline.isEmpty()) {
                            for (SiteAttributeValidatorModel.MapElementResult mapElementResult : metaPipeline) {
                                try {
                                    WebElement targetElement = refreshedElements.get(i).findElement(By.xpath(mapElementResult.getSelector()));
                                    String attribute = mapElementResult.getAttribute();
                                    String text = targetElement.getText();
                                    listPageMetadata.put(attribute, text);
                                } catch (Exception e) {
                                    System.out.println("Failed to map element: " + e.getMessage());
                                }
                            }
                        }
                        executeProcessFlowRecursive(refreshedElements.get(i), childPipelines, callback,listPageMetadata);
                    }
                }
            } else if (root != null && pipeline instanceof SiteAttributeValidatorModel.ClickOnElement navigateAction) {
                System.out.println("Clicking on element: " + root.getText());
                try {
                    String currentUrl = webDriver.getCurrentUrl();
                    root.click();
                    Thread.sleep(5000);
                    String newUrl = webDriver.getCurrentUrl();
                    if (currentUrl != null && !currentUrl.equals(newUrl)) {
                        System.out.println("Navigated to new URL: " + newUrl);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to click on element: " + e.getMessage());
                }
            } else if (pipeline instanceof SiteAttributeValidatorModel.BackToPreviousPage backToPreviousPage) {
                webDriver.navigate().back();
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                System.out.println("Navigated back to URL: " + webDriver.getCurrentUrl());
            } else if (pipeline instanceof SiteAttributeValidatorModel.MapElementResult mapElementResult) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                WebElement targetElement = webDriver.findElement(By.xpath(mapElementResult.getSelector()));
                String attribute = mapElementResult.getAttribute();
                String text = targetElement.getText();
                callback.onNewMappingAvailable(attribute, text);
            } else if (pipeline instanceof SiteAttributeValidatorModel.SaveJob saveJob) {
                if(jobMetaMappings!=null){
                    for (Map.Entry<String, String> entry : jobMetaMappings.entrySet()) {
                        System.out.println("Mapping: " + entry.getKey() + " -> " + entry.getValue());
                        if(entry.getValue() == null || entry.getValue().isEmpty()) {
                            System.out.println("Invalid mapping, skipping.");
                            continue;
                        }
                        callback.onNewMappingAvailable(entry.getKey(), entry.getValue());
                    }
                }
                callback.save();
            } else if (pipeline instanceof SiteAttributeValidatorModel.AskAI askAI) {
                Jobs jobs = new Jobs();
                String currentUrl = webDriver.getCurrentUrl();
                jobs.setJobUrl(currentUrl);
                jobs.setJobId(JobIndexEngine.cleanJobId(currentUrl));
                Jobs job = jobIndexingAdapter.getJobMetadata(jobs);
                Map <String,String> jobMappings = objectMapper.convertValue(job, new TypeReference<Map<String, String>>() {});
                jobMappings.entrySet().removeIf(e -> e.getValue() == null || e.getValue().isEmpty());
                for (String key : jobMappings.keySet()) {
                    System.out.println("Mapping: " + key + " -> " + jobMappings.get(key));
                    callback.onNewMappingAvailable(key, jobMappings.get(key));
                }
            }
        }
    }
}

interface JobExtractionCallback {
    void onNewMappingAvailable(String key, String output);

    void save();
}
