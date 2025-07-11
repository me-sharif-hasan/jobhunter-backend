package com.iishanto.jobhunterbackend.domain.service.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.domain.adapter.JobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.NotificationAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.SiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminJobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import com.iishanto.jobhunterbackend.domain.usecase.admin.JobIndexUseCase;
import com.iishanto.jobhunterbackend.infrastructure.crawler.CareerPageSpider;
import com.iishanto.jobhunterbackend.infrastructure.ports.indexing.JobIndexEngine;
import com.iishanto.jobhunterbackend.web.dto.response.indexing.JobIndexStrategyResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobIndexingService implements JobIndexUseCase {
    private final JobIndexingAdapter jobIndexingAdapter;
    private final AdminSiteDataAdapter adminSiteDataAdapter;
    private final ObjectMapper objectMapper;
    private final SiteDataAdapter siteDataAdapter;
    private final CareerPageSpider careerPageSpider;
    private final JobDataAdapter jobDataAdapter;
    private final AdminJobDataAdapter adminJobDataAdapter;
    private final JobIndexEngine jobIndexEngine;
    private final NotificationAdapter notificationAdapter;

    @Override
    public void refreshJobIndexWithStrategy(Long siteId) {
        Set <SimpleSiteModel> sitesToRefresh = siteId!=null&&siteId>0
                ? Set.of(adminSiteDataAdapter.getSiteById(siteId).orElseThrow(()->new IllegalArgumentException("Site ID Not Found")))
                : Set.copyOf(adminSiteDataAdapter.getSitesForIndexing());

        if (sitesToRefresh.isEmpty()) {
            throw new IllegalArgumentException("No sites found for indexing");
        }


        Map<Long,SiteAttributeValidatorModel> siteDataPipelineModel = jobIndexingAdapter.getSiteAttributeValidatorModels(
                sitesToRefresh.stream().map(SimpleSiteModel::getId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(SiteAttributeValidatorModel::getSiteId, model -> model));
        Queue <SimpleSiteModel> aiDependentSitesToIndex = new LinkedList<>();
        Set <String> foundJobIds = new HashSet<>();
        sitesToRefresh.forEach(site->{
            List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow = Optional.ofNullable(siteDataPipelineModel.get(site.getId())).orElseGet(()->SiteAttributeValidatorModel.builder().build()).getProcessFlow();
            if(processFlow==null|| processFlow.isEmpty()){
                System.out.println("Skipping indexing for site " + site.getName() + " as no attributes found");
                aiDependentSitesToIndex.add(site);
                return;
            }
            System.out.println("Indexing site: " + site.getName() + " with attributes: " + processFlow.size()+" steps");
            try{
                Set <String> availableJobIds=new HashSet<>();
                careerPageSpider.executeProcessFlow(site.getJobListPageUrl(), processFlow, jobModel -> {
                    jobModel.setSite(site);
                    jobModel.setJobDescription(StringUtils.trim(jobModel.getJobDescription()));
                    jobModel.setTitle(StringUtils.trim(jobModel.getTitle()));
                    jobModel.setJobUrl(StringUtils.trim(jobModel.getJobUrl()));
                    handleJob(jobModel);
                    availableJobIds.add(jobModel.getJobId());
                    foundJobIds.add(jobModel.getJobId());
                });
                adminJobDataAdapter.updateNonExistentJobsGivenFoundJobs(availableJobIds,site.getId());
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Manual Indexing completed for site: " + site.getName());
        });
        System.out.println("Manual Indexing completed for all sites, now indexing AI dependent sites");
        notificationAdapter.sendJobNotification(new ArrayList<>(foundJobIds));

        jobIndexEngine.runIndexingUnit(jobIds -> {
            foundJobIds.addAll(jobIds);
            notificationAdapter.sendJobNotification(new ArrayList<>(foundJobIds));
            System.out.println("AI dependent sites indexed, found " + jobIds.size() + " jobs");
        },aiDependentSitesToIndex);
    }

    private void handleJob(SimpleJobModel jobModel) {
        if(StringUtils.isBlank(jobModel.getJobId())){
            throw new IllegalArgumentException("Job ID cannot be empty");
        }
        Optional<SimpleJobModel> existingJob = adminJobDataAdapter.findJobById(jobModel.getJobId());
        if(existingJob.isPresent()){
            SimpleJobModel existing = existingJob.get();
            mergeNullFields(jobModel,existing);
            adminJobDataAdapter.updateIndexedJob(jobModel);
        }else{
            adminJobDataAdapter.saveJob(jobModel);
        }
    }

    private void mergeNullFields(SimpleJobModel to,SimpleJobModel from){
        to.setJobDescription(Optional.ofNullable(to.getJobDescription()).orElse(from.getJobDescription()));
        to.setJobUrl(Optional.ofNullable(to.getJobUrl()).orElse(from.getJobUrl()));
        to.setTitle(Optional.ofNullable(to.getTitle()).orElse(from.getTitle()));
        to.setJobType(Optional.ofNullable(to.getJobType()).orElse(from.getJobType()));
        to.setSalary(Optional.ofNullable(to.getSalary()).orElse(from.getSalary()));
        to.setLocation(Optional.ofNullable(to.getLocation()).orElse(from.getLocation()));
        to.setJobCategory(Optional.ofNullable(to.getJobCategory()).orElse(from.getJobCategory()));
        to.setJobPostedDate(Optional.ofNullable(to.getJobPostedDate()).orElse(from.getJobPostedDate()));
        to.setJobLastDate(Optional.ofNullable(to.getJobLastDate()).orElse(from.getJobLastDate()));
        to.setJobApplyLink(Optional.ofNullable(to.getJobApplyLink()).orElse(from.getJobApplyLink()));
        to.setJobApplyEmail(Optional.ofNullable(to.getJobApplyEmail()).orElse(from.getJobApplyEmail()));
        to.setJobParsedAt(Optional.ofNullable(to.getJobParsedAt()).orElse(from.getJobParsedAt()));
    }

    @Override
    public Long saveJobIndexStrategy(IndexingStrategyNames type, Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> pipeline) {
        Optional.ofNullable(adminSiteDataAdapter.getSiteById(siteId))
                .orElseThrow(() -> new RuntimeException("Site not found"));
        String jsonStrategy = objectMapper.valueToTree(pipeline).toString();
        Long savedId = adminSiteDataAdapter.saveIndexingStrategy(type,siteId, jsonStrategy);
        if (savedId == null || savedId <= 0) {
            throw new RuntimeException("Failed to save indexing strategy");
        }
        return savedId;
    }

    @Override
    public List<SimpleJobModel> validateStrategyAndGetJobs(Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow) {
        if(siteId == null || siteId <= 0) {
            throw new IllegalArgumentException("Invalid Site ID");
        }
        if(processFlow == null || processFlow.isEmpty()) {
            throw new IllegalArgumentException("Process flow cannot be null or empty");
        }
        SimpleSiteModel site = adminSiteDataAdapter.getSiteById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("Site ID Not Found"));
        List<SimpleJobModel> jobs = new ArrayList<>();
        careerPageSpider.executeProcessFlow(site.getJobListPageUrl(), processFlow, jobModel -> {
            jobModel.setSite(site);
            jobModel.setJobDescription(StringUtils.trim(jobModel.getJobDescription()));
            jobModel.setTitle(StringUtils.trim(jobModel.getTitle()));
            jobModel.setJobUrl(StringUtils.trim(jobModel.getJobUrl()));
            jobs.add(jobModel);
        });
        return jobs;
    }

    public interface OnJobAvailableCallback {
        void onJobAvailable(SimpleJobModel jobModel);
    }
}
