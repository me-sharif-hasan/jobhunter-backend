package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import com.iishanto.jobhunterbackend.domain.usecase.UserJobAccessUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.JobApplyManagementUseCase;
import com.iishanto.jobhunterbackend.scheduled.ScheduledJobIndexRefresher;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
@AllArgsConstructor
public class JobController {
    private final UserJobAccessUseCase userJobAccessUseCase;
    private final ScheduledJobIndexRefresher scheduledJobIndexRefresher;
    private final JobApplyManagementUseCase jobApplyManagementUseCase;


    @GetMapping("/refresh")
    public ApiResponse refreshJobs(){
        scheduledJobIndexRefresher.refreshJobIndex();
        return new ApiResponse(
                true,
                null,
                "Job Index Refreshed"
        );
    }

    @GetMapping
    public ApiResponse getJobs(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int limit,
            @RequestParam(defaultValue = "-1") int siteId,
            @RequestParam(defaultValue = "all") String variant
    ){
        if(page<0||limit<0||limit>50){
            throw new IllegalArgumentException("Invalid query parameters");
        }
        return new ApiResponse(
                true,
                switch (variant){
                    case "all"-> userJobAccessUseCase.getAllJobs(
                            page,limit,query, (long) siteId
                    );
                    case "applied" -> userJobAccessUseCase.getAppliedJobs(
                      page, limit, query, siteId
                    );
                    default -> userJobAccessUseCase.getSubscribedJobs(
                            page, limit, query, siteId
                    );
                },
                "Jobs fetched successfully"
        );
    }

    @GetMapping("/mark-applied")
    public ApiResponse markApplied(
            @RequestParam(name = "job_id") String jobId
    ){
        jobApplyManagementUseCase.markApplied(jobId);
        return new ApiResponse(
                true,
                null,
                "Job marked as applied successfully"
        );
    }

    @GetMapping("/unmark-applied")
    public ApiResponse unmarkApplied(
            @RequestParam(name = "job_id") String jobId
    ){
        jobApplyManagementUseCase.unmarkApplied(jobId);
        return new ApiResponse(
                true,
                null,
                "Job removed from applied successfully"
        );
    }

    @GetMapping("/get-applied-jobs")
    public ApiResponse getAppliedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String query
    ){
        if(page<0||limit<0||limit>50){
            throw new IllegalArgumentException("Invalid query parameters");
        }
        return new ApiResponse(
                true,
                jobApplyManagementUseCase.getAppliedJobs(page,limit,query),
                "Applied jobs fetched successfully"
        );
    }

    @GetMapping("/update-job-application-status")
    public ApiResponse updateJobApplicationStatus(
            @RequestParam(name = "job_id") String jobId,
            @RequestParam(name = "status") JobApplicationStatus status
    ){
        jobApplyManagementUseCase.updateJobApplicationStatus(jobId, status);
        return new ApiResponse(
                true,
                status,
                "Job application status updated successfully"
        );
    }
}
