package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleCalculatedResumeStrengthModel;

import java.io.IOException;
import java.io.InputStream;

public interface ResumeManagementUseCase {
    Long uploadResume(InputStream fileIo, String contentType) throws IOException;

    SimpleCalculatedResumeStrengthModel getResumeStrength(String jobId);
}
