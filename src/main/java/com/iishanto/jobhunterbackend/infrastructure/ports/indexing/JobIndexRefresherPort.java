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

import java.sql.Timestamp;
import java.util.Calendar;
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
        jobEntity.setJobId(job.getJobId());
        jobEntity.setJobUrl(job.getJobUrl());
        jobEntity.setDescriptionIndexed(true);
        return jobEntity;
    }

    @Override
    public void indexJob(OnIndexingDone onIndexingDone) {
        if(isIndexing) return;
        isIndexing=true;
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
                                jobEntity.setJobUrl(preservedUrl);
                            }
                            jobsRepository.save(jobEntity);
                            newJobIds.add(jobEntity.getJobId());
                        }else{
                            System.out.println("job already exits");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Indexing done");
            isIndexing=false;
            onIndexingDone.onIndexingDone(newJobIds);
        }).start();
    }

    private String cleanJobId(String jobId) {
        return StringUtils.stripAccents(jobId).replaceAll("[^a-zA-Z0-9]", "_");
    }

}
