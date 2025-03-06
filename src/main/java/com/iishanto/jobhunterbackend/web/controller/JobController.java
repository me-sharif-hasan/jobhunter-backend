package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.service.JobAggregatorService;
import com.iishanto.jobhunterbackend.domain.usecase.GetSubscribedJobsUseCase;
import com.iishanto.jobhunterbackend.scheduled.ScheduledJobIndexRefresher;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final GetSubscribedJobsUseCase getSubscribedJobsUseCase;
    private final ScheduledJobIndexRefresher scheduledJobIndexRefresher;
    private final JobAggregatorService jobAggregatorService;
    public JobController(JobAggregatorService jobAggregatorService,GetSubscribedJobsUseCase getSubscribedJobsUseCase,ScheduledJobIndexRefresher scheduledJobIndexRefresher) {
        this.getSubscribedJobsUseCase = getSubscribedJobsUseCase;
        this.scheduledJobIndexRefresher = scheduledJobIndexRefresher;
        this.jobAggregatorService=jobAggregatorService;
    }


    @GetMapping("/api/refresh")
    public ApiResponse refreshJobs(){
        scheduledJobIndexRefresher.refreshJobIndex();
        return new ApiResponse(
                true,
                null,
                "Job Index Refreshed"
        );
    }

    @GetMapping
    public ApiResponse getJobs(){
        return new ApiResponse(
                true,
                getSubscribedJobsUseCase.getSubscribedJobs(),
                "Jobs fetched successfully"
        );
    }
}
