package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.usecase.IndexJobs;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllJobsUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.JobUpdateUseCase;
import com.iishanto.jobhunterbackend.scheduled.ScheduledJobIndexRefresher;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/job")
@AllArgsConstructor
public class AdminJobController {
    ScheduledJobIndexRefresher refreshJobUseCase;
    GetAllJobsUseCase getAllJobsUseCase;
    JobUpdateUseCase jobUpdateUseCase;
    @GetMapping("/refresh")
    public void refreshJobs(){
        refreshJobUseCase.refreshJobIndex();
        System.out.println("Jobs Refreshed");
    }

    @PatchMapping("/mark-duplicate")
    public ApiResponse updateJob(
            @RequestParam String jobId,
            @RequestParam boolean isDuplicate
    ){
        jobUpdateUseCase.updateDuplicateStatus(jobId,isDuplicate);
        return new ApiResponse(
                true,
                jobId,
                "Job updated successfully"
        );
    }

    @GetMapping
    public ApiResponse getAllJobs(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int limit
    ){
        if(page<0||limit<0||limit>50){
            throw new IllegalArgumentException("Invalid query parameters");
        }
        ApiResponse apiResponse = new ApiResponse(
                true,
                getAllJobsUseCase.getAllJobs(
                        page,limit,query
                ),
                "Jobs fetched successfully"
        );
        apiResponse.setTotalRecords(
                getAllJobsUseCase.getTotalJobsCount()
        );
        return apiResponse;
    }
}
