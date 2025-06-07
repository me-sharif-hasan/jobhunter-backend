package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;
import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import com.iishanto.jobhunterbackend.domain.usecase.JobCommentUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.UserJobAccessUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.JobApplyManagementUseCase;
import com.iishanto.jobhunterbackend.scheduled.ScheduledJobIndexRefresher;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@AllArgsConstructor
public class JobController {
    private final UserJobAccessUseCase userJobAccessUseCase;
    private final ScheduledJobIndexRefresher scheduledJobIndexRefresher;

    private final JobApplyManagementUseCase jobApplyManagementUseCase;
    private final JobCommentUseCase jobCommentUseCase;


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
        List dd=userJobAccessUseCase.getAppliedJobs(
                page, limit, query, siteId
        );
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
        List <SimpleUserAppliedJobsModel> jobs = jobApplyManagementUseCase.getAppliedJobs(page,limit,query);
        System.out.println(jobs.get(0).getAppliedAt());
        return new ApiResponse(
                true,
                jobs,
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

    @PostMapping(
            path = "/comment"
    )
    public ApiResponse postComment(
            @RequestBody SimpleJobCommentModel comment
            ){
        String commentId = jobCommentUseCase.addJobComment(comment);
        return new ApiResponse(
                true,
                commentId,
                "Comment added successfully"
        );
    }

    @GetMapping(
            path = "/comment"
    )
    public ApiResponse getComment(
            @RequestParam(value = "job_id") String jobId,
            @RequestParam(value = "start_at",defaultValue = "0") Long startAt,
            @RequestParam(value = "limit",defaultValue = "20") Integer limit,
            @RequestParam(value = "parent_uuid",defaultValue = "") String parentUuid
    ){
        List<SimpleJobCommentModel> comments = jobCommentUseCase.getAllJobComments(jobId,limit,startAt);
        return new ApiResponse(
                true,
                comments,
                "Comment fetched successfully"
        );
    }
}
