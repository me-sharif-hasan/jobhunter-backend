package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;
import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;

import java.util.List;

public interface JobDataAdapter {
    void markApplied(String jobId, Long userId);
    void unmarkApplied(String jobId, Long id);
    List<SimpleUserAppliedJobsModel> getAppliedJobs(Long id, int page, int limit, String query);
    void updateJobApplicationStatus(String jobId, Long userId, JobApplicationStatus status);
    SimpleJobCommentModel postComment(SimpleJobCommentModel model);
    List<SimpleJobCommentModel> getJobComments(String jobId, int limit, Long startAt);
    SimpleJobModel getJobById(String jobId);
}
