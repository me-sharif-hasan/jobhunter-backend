package com.iishanto.jobhunterbackend.domain.usecase;

public interface JobApplyManagementUseCase {
    void markApplied(String jobId);

    void unmarkApplied(String jobId);
}
