package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.ResumeManagementUseCase;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeManagementUseCase resumeManagementUseCase;
    @PostMapping("/upload")
    public ApiResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ApiResponse(
                    false,
                    null,
                    "File is empty"
            );
        }
        Long fileId = resumeManagementUseCase.uploadResume(file.getInputStream(),file.getContentType());
        return new ApiResponse(
                true,
                fileId,
                "File uploaded successfully"
        );
    }

    @GetMapping("/me-vs-job")
    public ApiResponse measureResumeStrength(
            @RequestParam("job_id") String jobId){
        if (jobId == null) {
            return new ApiResponse(
                    false,
                    null,
                    "Job ID must be provided"
            );
        }
        try {
            return new ApiResponse(
                    true,
                    resumeManagementUseCase.getResumeStrength(jobId),
                    "Resume strength calculated successfully"
            );
        } catch (Exception e) {
            return new ApiResponse(
                    false,
                    null,
                    "Error calculating resume strength: " + e.getMessage()
            );
        }
    }
}
