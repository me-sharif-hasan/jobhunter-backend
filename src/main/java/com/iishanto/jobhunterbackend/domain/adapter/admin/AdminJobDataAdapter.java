package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminJobDataAdapter {
    List<SimpleJobModel> getAllJobsForAdmin(int page, int limit, String query);

    long getTotalJobsCount();
    void updateJobDuplicateStatus(String jobId, boolean isDuplicate);

    void updateIndexedJob(SimpleJobModel simpleJobModel);
    void updateJob(SimpleJobModel simpleJobModel);

    Optional<SimpleJobModel> findJobById(String jobId);

    void saveJob(SimpleJobModel jobModel);

    void updateNonExistentJobsGivenFoundJobs(Set<String> availableJobIds,Long siteId);
}

