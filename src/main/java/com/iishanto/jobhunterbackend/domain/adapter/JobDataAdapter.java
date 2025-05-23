package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;

import java.util.List;

public interface JobDataAdapter {
    void markApplied(String jobId, Long userId);

    void unmarkApplied(String jobId, Long id);

    List<SimpleUserAppliedJobsModel> getAppliedJobs(Long id, int page, int limit, String query);
}
