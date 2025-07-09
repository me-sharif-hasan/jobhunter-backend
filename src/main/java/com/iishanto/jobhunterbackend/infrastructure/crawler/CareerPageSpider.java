package com.iishanto.jobhunterbackend.infrastructure.crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.service.admin.JobIndexingService;
import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;
import com.iishanto.jobhunterbackend.infrastructure.ports.indexing.JobIndexEngine;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public void executeProcessFlow(String url, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow, JobIndexingService.OnJobAvailableCallback onJobAvailable) {
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
            public Map<String, String> getJobMappings() {
                return jobMappings;
            }

            @Override
            public void save() {
                System.out.println("Saving mappings...");
                Opportunity opportunity = objectMapper.convertValue(jobMappings, Opportunity.class);
                opportunity.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
                opportunity.setIsPresentOnSite(true);
                opportunity.setDescriptionIndexed(true);
                opportunity.setLastSeenAt(Timestamp.valueOf(LocalDateTime.now()));
                opportunity.setJobUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                onJobAvailable.onJobAvailable(opportunity.toSimpleJobModel());
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
                String selector = navigateAction.getSelector();
                if(!StringUtils.isBlank(selector)) {
                    root= root.findElement(By.xpath(selector));
                }
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
                if(!StringUtils.isBlank(mapElementResult.getJavaScript())&&!StringUtils.isBlank(mapElementResult.getSelector())) {
                    throw new IllegalArgumentException("Both JavaScript and Selector cannot be set at the same time.");
                }
                String text;
                String attribute = mapElementResult.getAttribute();
                if(!StringUtils.isBlank(mapElementResult.getSelector())) {
                    WebElement targetElement = root.findElement(By.xpath(mapElementResult.getSelector()));
                    text = targetElement.getAttribute("textContent");
                }else{
                    String js = """
                            return (()=>{
                            %s
                            })();
                            """.formatted(mapElementResult.getJavaScript());
                    js = processStringTemplate(callback.getJobMappings(),js);
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
                    text = (String) jsExecutor.executeScript(js);
                }
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
                try{
                    callback.save();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (pipeline instanceof SiteAttributeValidatorModel.AskAI askAI) {
                Opportunity opportunity = new Opportunity();
                String currentUrl = webDriver.getCurrentUrl();
                opportunity.setJobUrl(currentUrl);
                opportunity.setJobId(JobIndexEngine.cleanJobId(currentUrl));
                String baseContext = processStringTemplate(callback.getJobMappings(),askAI.getWithContext());
                Opportunity job = jobIndexingAdapter.getJobMetadata(opportunity, baseContext);
                Map <String,String> jobMappings = objectMapper.convertValue(job, new TypeReference<>() {
                });
                jobMappings.entrySet().removeIf(e -> e.getValue() == null || e.getValue().isEmpty());
                for (String key : jobMappings.keySet()) {
                    System.out.println("Mapping: " + key + " -> " + jobMappings.get(key));
                    callback.onNewMappingAvailable(key, jobMappings.get(key));
                }
            }
        }
    }

    private String processStringTemplate(Map<String, String> mappings,String template) {
        if(StringUtils.isBlank(template)) {
            return "";
        }
        List<String> patterns = mappings.keySet().stream().map(key->"%"+key+"%").toList();
        List<String> values = mappings.values().stream().toList();
        for (int i = 0; i < patterns.size(); i++) {
            template = template.replaceAll(patterns.get(i), values.get(i));
        }
        return template;
    }
}

interface JobExtractionCallback {
    void onNewMappingAvailable(String key, String output);
    Map<String,String> getJobMappings();
    void save();
}
