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

import java.sql.Date;
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
        Jobs jobEntity=Jobs.fromSimpleJobModel(jobModel, job.getSite(),true);
        jobEntity.setJobId(job.getJobId());
        jobEntity.setJobUrl(job.getJobUrl());
        jobEntity.setDescriptionIndexed(true);
        return jobEntity;
    }

    @Override
    public void indexJob(OnIndexingDone onIndexingDone) {
        if(isIndexing) return;
        isIndexing=true;
        systemStatusService.updateJobIndexingStatus(SystemStatusValues.JOB_INDEXING);
        List <String> newJobIds=new LinkedList<>();
        new Thread(()->{
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
                        if(!jobsRepository.existsById(jobEntity.getJobId())&&!jobsRepository.existsByJobUrl(jobEntity.getJobUrl())){
                            System.out.println("job not exits"+jobEntity.getJobId()+" "+jobEntity.getJobUrl());
                            if(jobEntity.getJobUrl()!=null&&!jobEntity.isDescriptionIndexed()){
                                String preservedUrl=jobEntity.getJobUrl();
                                jobEntity=this.getJobMetadata(jobEntity);
                                if(!StringUtils.isBlank(jobEntity.getJobLastDate())){
                                    Timestamp metaNormalizedDate=DateNormalizer.normalizeToTimestamp(jobEntity.getJobLastDate());
                                    if (normalizedDate!=null){
                                        jobEntity.setJobLastDate(metaNormalizedDate.toString());
                                    }
                                }
                                jobEntity.setJobUrl(preservedUrl);
                            }
                            jobEntity.setLastSeenAt(new Timestamp(System.currentTimeMillis()));
                            jobsRepository.save(jobEntity);
                            newJobIds.add(jobEntity.getJobId());
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
                                jobsRepository.save(existingJob);
                            }
                            System.out.println("job already exits");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    List<Jobs> jobs=jobsRepository.findAll();
                    for(Jobs job:jobs){
                        if(StringUtils.isBlank(job.getJobLastDate())){
                            Timestamp lastSeenAt=job.getLastSeenAt();
                            if(lastSeenAt!=null){
                                long diff = System.currentTimeMillis() - lastSeenAt.getTime();
                                long diffHours = diff / (60 * 60 * 1000);
                                if(diffHours>24){
                                    job.setJobLastDate(job.getLastSeenAt().toString());
                                    jobsRepository.save(job);
                                }
                            }

                        }
                    }
                }
            }
            System.out.println("Indexing done");
            isIndexing=false;
            systemStatusService.updateJobIndexingStatus(SystemStatusValues.JOB_INDEXER_IDLE);
            onIndexingDone.onIndexingDone(newJobIds);
        }).start();
    }

    private String cleanJobId(String jobId) {
        return StringUtils.stripAccents(jobId).replaceAll("[^a-zA-Z0-9]", "_");
    }

}
