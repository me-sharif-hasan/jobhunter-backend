package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.usecase.admin.JobIndexUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.JobIndexStrategySaveRequestDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/indexing")
public class IndexingController {
    private final JobIndexUseCase jobIndexUseCase;
    @GetMapping("/refresh")
    public void refreshJobIndex(
            @RequestParam(name = "site_id",defaultValue = "-1") Long siteId
    ) {
        jobIndexUseCase.refreshJobIndexWithStrategy(siteId);
        System.out.println("Job Index Refreshed");
    }

    @PostMapping("/save-strategy")
    public ApiResponse saveJobIndexStrategy(
            @RequestBody JobIndexStrategySaveRequestDto strategy,
            @RequestParam(name = "site_id") Long siteId
            ){
        Long savedId = jobIndexUseCase.saveJobIndexStrategy(siteId, strategy.getProcessFlow());
        if (savedId == null || savedId <= 0) {
            throw new RuntimeException("Failed to save indexing strategy");
        }
        return new ApiResponse(
                true,
                savedId,
                "Job Indexing Strategy saved successfully"
        );
    }
}
