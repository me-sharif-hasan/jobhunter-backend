package com.iishanto.jobhunterbackend.domain.adapter;

public interface JobDataAdapter {
    void markApplied(String jobId, Long userId);

    void unmarkApplied(String jobId, Long id);
}
