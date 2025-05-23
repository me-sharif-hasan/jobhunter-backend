package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;

import java.util.List;

public interface JobApplyManagementUseCase {
    void markApplied(String jobId);

    void unmarkApplied(String jobId);

    List<SimpleUserAppliedJobsModel> getAppliedJobs(int page, int limit, String query);
}
