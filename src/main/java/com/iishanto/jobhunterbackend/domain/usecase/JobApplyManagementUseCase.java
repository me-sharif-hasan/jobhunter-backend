package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;
import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;

import java.util.List;

public interface JobApplyManagementUseCase {
    void markApplied(String jobId);

    void unmarkApplied(String jobId);

    List<SimpleUserAppliedJobsModel> getAppliedJobs(int page, int limit, String query);
    void updateJobApplicationStatus(String jobId, JobApplicationStatus status);
}
