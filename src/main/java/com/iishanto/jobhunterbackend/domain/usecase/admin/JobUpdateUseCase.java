package com.iishanto.jobhunterbackend.domain.usecase.admin;

public interface JobUpdateUseCase {
    void updateDuplicateStatus(String jobId, boolean isDuplicate);
    void approveJob(String jobId);
}
