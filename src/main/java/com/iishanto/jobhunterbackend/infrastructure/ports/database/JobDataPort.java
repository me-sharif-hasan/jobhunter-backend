package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminJobDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JobDataPort implements AdminJobDataAdapter {
    JobsRepository jobsRepository;
    @Override
    public List<SimpleJobModel> getAllJobsForAdmin(int page, int limit, String query) {
        Pageable pageable = PageRequest.of(page, limit);
        List<Jobs> jobs = jobsRepository.findAllByJobDescriptionContainingOrTitleContainingOrLocationContainingOrderByJobParsedAtDesc(query,query,query,pageable);
        return jobs.stream().map(Jobs::toSimpleJobModel).collect(Collectors.toList());
    }

    @Override
    public long getTotalJobsCount() {
        return jobsRepository.count();
    }

    @Override
    public void updateJobDuplicateStatus(String jobId, boolean isDuplicate) {
        Jobs job = jobsRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        job.setDuplicate(isDuplicate);
        jobsRepository.save(job);
    }

    @Override
    public void updateJob(SimpleJobModel simpleJobModel) {
        Optional <Jobs> optionalJob = jobsRepository.findById(simpleJobModel.getJobId());
        if (optionalJob.isPresent()) {
            Jobs job = optionalJob.get();
            job.setJobType(simpleJobModel.getJobType());
            job.setSalary(simpleJobModel.getSalary());
            job.setLocation(simpleJobModel.getLocation());
            job.setJobUrl(simpleJobModel.getJobUrl());
            job.setTitle(simpleJobModel.getTitle());
            job.setJobCategory(simpleJobModel.getJobCategory());
            job.setJobDescription(simpleJobModel.getJobDescription());
            job.setJobPostedDate(simpleJobModel.getJobPostedDate());
            job.setJobLastDate(simpleJobModel.getJobLastDate());
            job.setJobApplyLink(simpleJobModel.getJobApplyLink());
            job.setJobApplyEmail(simpleJobModel.getJobApplyEmail());
            jobsRepository.save(job);
        } else {
            throw new RuntimeException("Job not found");
        }
    }
}
