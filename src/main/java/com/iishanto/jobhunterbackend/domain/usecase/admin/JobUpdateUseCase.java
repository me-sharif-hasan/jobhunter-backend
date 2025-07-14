package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

public interface JobUpdateUseCase {
    void updateDuplicateStatus(String jobId, boolean isDuplicate);
    void approveJob(String jobId);
    void rejectJob(String jobId);
    void updateJob(String jobId, SimpleJobModel simpleJobModel);
}
