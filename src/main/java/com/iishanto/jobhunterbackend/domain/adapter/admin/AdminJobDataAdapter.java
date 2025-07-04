package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;

public interface AdminJobDataAdapter {
    List<SimpleJobModel> getAllJobsForAdmin(int page, int limit, String query);

    long getTotalJobsCount();
    void updateJobDuplicateStatus(String jobId, boolean isDuplicate);

    void updateJob(SimpleJobModel simpleJobModel);
}
