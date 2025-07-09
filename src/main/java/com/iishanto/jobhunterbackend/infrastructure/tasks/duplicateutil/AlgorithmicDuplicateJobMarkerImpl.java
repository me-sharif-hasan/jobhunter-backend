package com.iishanto.jobhunterbackend.infrastructure.tasks.duplicateutil;

import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlgorithmicDuplicateJobMarkerImpl implements DuplicateJobMarker{
    JobsRepository jobsRepository;
    SiteRepository siteRepository;
    @Override
    public void findDuplicates() {

    }

    @Override
    public void markDuplicates() {

    }

    @Override
    public void findAndMarkDuplicates() {

    }

    @Override
    public void isDuplicate(Opportunity opportunity) {
        //reload the job from the database
        Opportunity job = jobsRepository.findById(opportunity.getJobId()).orElseThrow(() -> new RuntimeException("Job not found"));
        //reload site related to the job
        Site site = siteRepository.findById(job.getSite().getId()).orElseThrow(() -> new RuntimeException("Site not found"));
        //get atleast one matching job from the site
//        Jobs matchingJob = jobsRepository.getDuplicateOfJob(job.getTitle(),job.getJobPostedDate(),job.getJobUrl(),job.getSite());
    }
}
