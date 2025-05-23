package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.service.JobAggregatorService;
import com.iishanto.jobhunterbackend.domain.usecase.GetSubscribedJobsUseCase;
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
    private final GetSubscribedJobsUseCase getSubscribedJobsUseCase;
    private final ScheduledJobIndexRefresher scheduledJobIndexRefresher;
    private final JobApplyManagementUseCase jobApplyManagementUseCase;
//    public JobController(GetSubscribedJobsUseCase getSubscribedJobsUseCase,ScheduledJobIndexRefresher scheduledJobIndexRefresher) {
//        this.getSubscribedJobsUseCase = getSubscribedJobsUseCase;
//        this.scheduledJobIndexRefresher = scheduledJobIndexRefresher;
//    }


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
            @RequestParam(defaultValue = "-1") int siteId
    ){
        if(page<0||limit<0||limit>50){
            throw new IllegalArgumentException("Invalid query parameters");
        }
        return new ApiResponse(
                true,
                getSubscribedJobsUseCase.getSubscribedJobs(
                        page,limit,query,siteId
                ),
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
}
