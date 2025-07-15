package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.usecase.ResumeManagementUseCase;

public class ResumeService implements ResumeManagementUseCase {
    @Override
    public Long uploadResume(String fileName, byte[] fileContent) {
        return 0L;
    }
}
