package com.iishanto.jobhunterbackend.infrastructure.ports.indexing;

import com.iishanto.jobhunterbackend.config.HunterUtility;
import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.values.SystemStatusValues;
import com.iishanto.jobhunterbackend.domain.service.admin.SystemStatusService;
import com.iishanto.jobhunterbackend.domain.utility.DateNormalizer;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.google.GeminiClient;
import com.iishanto.jobhunterbackend.infrastructure.google.GeminiPromptLibrary;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class JobIndexEngine implements JobIndexingAdapter {
    private final SiteRepository siteRepository;
    private final JobsRepository jobsRepository;
    HunterUtility hunterUtility;
    private final SystemStatusService systemStatusService;
    private final
    GeminiClient geminiClient;
    public JobIndexEngine(SiteRepository siteRepository, JobsRepository jobsRepository, GeminiClient geminiClient, HunterUtility hunterUtility, SystemStatusService systemStatusService) {
        this.siteRepository=siteRepository;
        this.geminiClient=geminiClient;
        this.jobsRepository=jobsRepository;
        this.hunterUtility=hunterUtility;
        this.systemStatusService=systemStatusService;
    }
    Queue < Site > indexingQueue=new LinkedList<>();
    boolean isIndexing=false;
    @Override
    public void refreshIndexingQueue() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24);
        Timestamp timestamp24HoursAgo = new Timestamp(calendar.getTimeInMillis());
        List < Site > sites = siteRepository.findAllByLastCrawledAtBefore(timestamp24HoursAgo);
        indexingQueue.addAll(sites);
    }


    @Override
    public Jobs getJobMetadata(Jobs job){
        System.out.println("Getting Metadata for: "+job.getJobId());
        GeminiClient.GeminiPrompt prompt = geminiClient.getMetadataPromptFromUrl(job.getJobUrl());
        if (prompt==null) return job;
        System.out.println("THE PROMPT"+prompt.getPromptTemplate(GeminiPromptLibrary.PromptType.JOB_DETAIL));
        SimpleJobModel jobModel = geminiClient.getJobMetadata(prompt);
        System.out.println("the job model"+jobModel);
        if(jobModel==null) return job;
        Jobs jobEntity=Jobs.fromSimpleJobModel(jobModel, job.getSite());
        mergeNullFields(jobEntity,job);
        jobEntity.setJobId(job.getJobId());
        jobEntity.setJobUrl(job.getJobUrl());
        jobEntity.setDescriptionIndexed(true);
        return jobEntity;
    }

    private void mergeNullFields(Jobs jobEntity, Jobs initialJob) {
        if (jobEntity.getTitle() == null) {
            jobEntity.setTitle(initialJob.getTitle());
        }
        if (jobEntity.getJobUrl() == null) {
            jobEntity.setJobUrl(initialJob.getJobUrl());
        }
        if (jobEntity.getLocation() == null) {
            jobEntity.setLocation(initialJob.getLocation());
        }
        if (jobEntity.getSalary() == null) {
            jobEntity.setSalary(initialJob.getSalary());
        }
        if (jobEntity.getJobType() == null) {
            jobEntity.setJobType(initialJob.getJobType());
        }
        if (jobEntity.getJobCategory() == null) {
            jobEntity.setJobCategory(initialJob.getJobCategory());
        }
        if (jobEntity.getJobDescription() == null) {
            jobEntity.setJobDescription(initialJob.getJobDescription());
        }
        if (jobEntity.getSkillsNeeded() == null) {
            jobEntity.setSkillsNeeded(initialJob.getSkillsNeeded());
        }
        if (jobEntity.getExperienceNeeded() == null) {
            jobEntity.setExperienceNeeded(initialJob.getExperienceNeeded());
        }
        if (jobEntity.getJobPostedDate() == null) {
            jobEntity.setJobPostedDate(initialJob.getJobPostedDate());
        }
        if (jobEntity.getJobLastDate() == null) {
            jobEntity.setJobLastDate(initialJob.getJobLastDate());
        }
        if (jobEntity.getJobApplyLink() == null) {
            jobEntity.setJobApplyLink(initialJob.getJobApplyLink());
        }
        if (jobEntity.getJobApplyEmail() == null) {
            jobEntity.setJobApplyEmail(initialJob.getJobApplyEmail());
        }
        if (jobEntity.getLastSeenAt() == null) {
            jobEntity.setLastSeenAt(initialJob.getLastSeenAt());
        }
        if (jobEntity.getSite() == null) {
            jobEntity.setSite(initialJob.getSite());
        }
    }

    @Override
    public void indexJob(OnIndexingDone onIndexingDone) {
        if(isIndexing) return;
        isIndexing=true;
        systemStatusService.updateJobIndexingStatus(SystemStatusValues.JOB_INDEXING);
        new Thread(()->{
            runIndexingUnit(onIndexingDone);
        }).start();
    }

    public void runIndexingUnit(OnIndexingDone onIndexingDone) {
        List <String> newJobIds=new LinkedList<>();
        Set <String> foundJobIds=new HashSet<>();
        while (!indexingQueue.isEmpty()){
            try{
                Site site=indexingQueue.poll();
                System.out.println("Indexing: "+site.getHomepage());
                GeminiClient.GeminiPrompt prompt = geminiClient.getJobListingPromptFromUrl(site.getJobListPageUrl());
                if(prompt==null) continue;
                site.setLastCrawledAt(new Timestamp(System.currentTimeMillis()));
                siteRepository.save(site);
                List < SimpleJobModel > jobs = geminiClient.getJsonResponseOfJobs(prompt);
                if(jobs==null) continue;
                for(SimpleJobModel job:jobs){
                    System.out.println("Job: "+job);
                    Timestamp normalizedDate=DateNormalizer.normalizeToTimestamp(job.getJobLastDate());
                    if (normalizedDate!=null){
                        job.setJobLastDate(normalizedDate.toString());
                    }
                    Jobs jobEntity=Jobs.fromSimpleJobModel(job,site);
                    if(jobEntity.getJobId()!=null){
                        jobEntity.setJobId(site.getJobListPageUrl()+"/"+jobEntity.getJobId());
                    }else{
                        jobEntity.setJobId(jobEntity.getJobUrl());
                    }
                    jobEntity.setJobId(cleanJobId(jobEntity.getJobId()));
                    jobEntity.setIsPresentOnSite(true);
                    if(!jobsRepository.existsById(jobEntity.getJobId())&&!jobsRepository.existsByJobUrl(jobEntity.getJobUrl())){
                        System.out.println("job not exits"+jobEntity.getJobId()+" "+jobEntity.getJobUrl());
                        if(jobEntity.getJobUrl()!=null&&!jobEntity.isDescriptionIndexed()){
                            String preservedUrl=jobEntity.getJobUrl();
                            jobEntity=this.getJobMetadata(jobEntity);
                            if(!StringUtils.isBlank(jobEntity.getJobLastDate())){
                                Timestamp metaNormalizedDate=DateNormalizer.normalizeToTimestamp(jobEntity.getJobLastDate());
                                if (metaNormalizedDate!=null){
                                    jobEntity.setJobLastDate(metaNormalizedDate.toString());
                                }
                            }
                            jobEntity.setJobUrl(preservedUrl);
                        }
                        jobEntity.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
                        jobEntity.setIsPresentOnSite(true);
                        jobsRepository.save(jobEntity);
                        newJobIds.add(jobEntity.getJobId());
                        foundJobIds.add(jobEntity.getJobId());
                    }else{
                        Optional<Jobs> optionalJob=jobsRepository.findById(jobEntity.getJobId());
                        if(optionalJob.isEmpty()){
                            optionalJob=jobsRepository.findByJobUrl(jobEntity.getJobUrl());
                        }
                        if(optionalJob.isPresent()){
                            Jobs existingJob=optionalJob.get();
                            existingJob.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
                            Timestamp updatedTimeFormat = DateNormalizer.normalizeToTimestamp(existingJob.getJobLastDate());

                            //if deadline is extended, update the last seen date
                            if(
                                !StringUtils.isBlank(jobEntity.getJobLastDate())
                                &&!jobEntity.getJobLastDate().equals(existingJob.getJobLastDate())
                            ) {
                                existingJob.setJobLastDate(jobEntity.getJobLastDate());
                            }else if(updatedTimeFormat!=null&&!updatedTimeFormat.toString().equals(existingJob.getJobLastDate())){
                                existingJob.setJobLastDate(updatedTimeFormat.toString());
                            }
                            if(existingJob.getIsPresentOnSite()==false){
                                existingJob = this.getJobMetadata(existingJob);
                                existingJob.setIsReopened(true);
                                existingJob.setIsPresentOnSite(true);
                                existingJob.setVersion(existingJob.getVersion()+1);
                            }
                            foundJobIds.add(existingJob.getJobId());
                            jobsRepository.save(existingJob);
                        }
                        System.out.println("job already exits");
                    }
                }
                updateNonExistentJobs(foundJobIds,site.getId());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Indexing done");
        isIndexing=false;
        systemStatusService.updateJobIndexingStatus(SystemStatusValues.JOB_INDEXER_IDLE);
        onIndexingDone.onIndexingDone(newJobIds);
    }

    private String cleanJobId(String jobId) {
        return StringUtils.stripAccents(jobId).replaceAll("[^a-zA-Z0-9]", "_");
    }

    void updateNonExistentJobs(Set<String> foundJobIds,Long siteId) {
        List <Jobs> jobs = jobsRepository.findJobsByJobIdNotInAndSiteId(foundJobIds, siteId);
        if (jobs.isEmpty()) return;
        System.out.println("Updating non-existent jobs: " + jobs.size());
        jobs.forEach(j->{
            j.setIsPresentOnSite(false);
            j.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
            j.setVersion(j.getVersion()+1);
        });
        jobsRepository.saveAll(jobs);
    }
}
