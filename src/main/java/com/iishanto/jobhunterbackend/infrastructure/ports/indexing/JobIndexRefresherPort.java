package com.iishanto.jobhunterbackend.infrastructure.ports.indexing;

import com.iishanto.jobhunterbackend.config.HunterUtility;
import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.gemini.GeminiClient;
import com.iishanto.jobhunterbackend.infrastructure.gemini.GeminiPromptLibrary;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class JobIndexRefresherPort implements JobIndexingAdapter {
    private final SiteRepository siteRepository;
    private final JobsRepository jobsRepository;
    HunterUtility hunterUtility;
    private final
    GeminiClient geminiClient;
    public JobIndexRefresherPort(SiteRepository siteRepository, JobsRepository jobsRepository,GeminiClient geminiClient,HunterUtility hunterUtility){
        this.siteRepository=siteRepository;
        this.geminiClient=geminiClient;
        this.jobsRepository=jobsRepository;
        this.hunterUtility=hunterUtility;
    }
    Queue < Site > indexingQueue=new LinkedList<>();
    Queue < Jobs > indexingJobsQueue=new LinkedList<>();
    boolean isIndexing=false;
    @Override
    public void refreshIndexingQueue() {
        List < Site > sites = siteRepository.findAllByOrderByCreatedAtDesc();
        indexingQueue.addAll(sites);
    }

    private void addJobForIndexing(Jobs job) {
        System.out.println("Adding job for indexing");
        if(job!=null&&!job.isDescriptionIndexed()){
            indexingJobsQueue.add(job);
            getJobMetadata();
        }else if(job!=null){
            System.out.println("Job already indexed: "+job.getJobId());
        }
    }

    @Override
    public Jobs getJobMetadata(Jobs job){
        System.out.println("Getting Metadata for: "+job.getJobId());
        GeminiClient.GeminiPrompt prompt = geminiClient.getMetadataPromptFromUrl(job.getJobUrl());
        if (prompt==null) return job;
        System.out.println("THE PROMPT"+prompt.getPromptTemplate(GeminiPromptLibrary.PromptType.JOB_DETAIL));
        SimpleJobModel jobModel = geminiClient.getJobMetadata(prompt);
        System.out.println("the job model"+jobModel);
        Jobs jobEntity=Jobs.fromSimpleJobModel(jobModel, job.getSite());
        jobEntity.setJobId(job.getJobId());
        jobEntity.setDescriptionIndexed(true);
        return jobEntity;
    }

    boolean isGettingJobMetadata=false;
    private void getJobMetadata(){
        if (isGettingJobMetadata) return;
        System.out.println("Starting metadata fetcher");
        isGettingJobMetadata=true;
        new Thread(()->{
            while (!indexingJobsQueue.isEmpty()){
                try{
                    Jobs job=indexingJobsQueue.poll();
                    GeminiClient.GeminiPrompt prompt = geminiClient.getMetadataPromptFromUrl(job.getJobUrl());
                    if (prompt==null) return;
                    SimpleJobModel jobModel = geminiClient.getJobMetadata(prompt);
                    Jobs jobEntity=Jobs.fromSimpleJobModel(jobModel, job.getSite());
                    jobEntity.setJobId(job.getJobId());
                    jobEntity.setDescriptionIndexed(true);
                    jobsRepository.save(jobEntity);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            isGettingJobMetadata=false;
        }).start();
    }

    @Override
    public void indexJob() {
        if(isIndexing) return;
        isIndexing=true;
        new Thread(()->{
            while (!indexingQueue.isEmpty()){
                try{
                    Site site=indexingQueue.poll();
                    System.out.println("Indexing: "+site.getHomepage());
                    List <Jobs> toBeAddedOrUpdated=new LinkedList<>();
                    GeminiClient.GeminiPrompt prompt = geminiClient.getJobListingPromptFromUrl(site.getJobListPageUrl());
                    if(prompt==null) return;
                    List < SimpleJobModel > jobs = geminiClient.getJsonResponseOfJobs(prompt);
                    for(SimpleJobModel job:jobs){
                        System.out.println("Job: "+job);
                        Jobs jobEntity=Jobs.fromSimpleJobModel(job,site);
                        if(jobEntity.getJobUrl()!=null){
                            jobEntity.setJobId(hunterUtility.MD5(jobEntity.getJobUrl()));
                        }
                        if(!jobsRepository.existsById(jobEntity.getJobId())){
                            System.out.println("job not exits"+jobEntity.getJobId()+" "+jobEntity.getJobUrl());
                            if(jobEntity.getJobUrl()!=null&&!jobEntity.isDescriptionIndexed()){
                                jobEntity=this.getJobMetadata(jobEntity);
                            }
                            jobsRepository.save(jobEntity);
//                            toBeAddedOrUpdated.add(jobEntity);
                        }else{
                            System.out.println("job already exits");
                        }
                    }
//                    jobsRepository.saveAll(toBeAddedOrUpdated);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            isIndexing=false;
        }).start();
    }
}
