package com.iishanto.jobhunterbackend.domain.usecase;

public interface ResumeManagementUseCase {
    Long uploadResume(String fileName, byte[] fileContent);
}
