package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminJobDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllJobsUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.JobUpdateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminJobService implements GetAllJobsUseCase, JobUpdateUseCase {
    AdminJobDataAdapter adminJobDataAdapter;
    @Override
    public List<SimpleJobModel> getAllJobs(int page, int limit, String query) {
        return adminJobDataAdapter.getAllJobsForAdmin(page, limit, query);
    }

    @Override
    public long getTotalJobsCount() {
        return adminJobDataAdapter.getTotalJobsCount();
    }

    @Override
    public void updateDuplicateStatus(String jobId, boolean isDuplicate) {
        if(jobId == null || jobId.isEmpty()) {
            throw new IllegalArgumentException("Job ID cannot be null or empty");
        }
        adminJobDataAdapter.updateJobDuplicateStatus(jobId, isDuplicate);
    }

    @Override
    public void approveJob(String jobId) {

    }

    @Override
    public void updateJob(String jobId, SimpleJobModel simpleJobModel) {
        simpleJobModel.setJobId(jobId);
        if(jobId == null || jobId.isEmpty()) {
            throw new IllegalArgumentException("Job ID cannot be null or empty");
        }
        adminJobDataAdapter.updateJob(simpleJobModel);
    }
}

