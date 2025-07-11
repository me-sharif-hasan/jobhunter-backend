package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteIndexingStrategyCompositionModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetSiteStrategyUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.JobIndexUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.JobIndexStrategySaveRequestDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import com.iishanto.jobhunterbackend.web.dto.response.indexing.JobIndexStrategyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/indexing")
public class IndexingController {
    private final JobIndexUseCase jobIndexUseCase;
    private final GetSiteStrategyUseCase getSiteStrategyUseCase;
    @GetMapping("/refresh")
    public ApiResponse refreshJobIndex(
            @RequestParam(name = "site_id",defaultValue = "-1") Long siteId
    ) {
        jobIndexUseCase.refreshJobIndexWithStrategy(siteId);
        System.out.println("Job Index Refreshed");
        return new ApiResponse(
                true,
                null,
                "Job index refreshed"
        );
    }

    @PostMapping("/save-strategy")
    public ApiResponse saveJobIndexStrategy(
            @RequestBody JobIndexStrategySaveRequestDto strategy,
            @RequestParam(name = "site_id") Long siteId
            ){
        Long savedId = jobIndexUseCase.saveJobIndexStrategy(strategy.getType(),siteId, strategy.getProcessFlow());
        if (savedId == null || savedId <= 0) {
            throw new RuntimeException("Failed to save indexing strategy");
        }
        return new ApiResponse(
                true,
                savedId,
                "Job Indexing Strategy saved successfully"
        );
    }

    @GetMapping("/get-strategy")
    public ApiResponse getJobIndexingStrategies(
            @RequestParam(name = "site_id") Long siteId
    ) {
        if (Objects.isNull(siteId) || siteId <= 0) {
            throw new IllegalArgumentException("Invalid site ID provided");
        }
        SimpleSiteIndexingStrategyCompositionModel siteStrategy = getSiteStrategyUseCase.getSiteStrategy(siteId);
        return new ApiResponse(
                true,
                JobIndexStrategyResponseDto.builder()
                        .type(siteStrategy.getIndexingStrategy())
                        .processFlow(siteStrategy.getStrategyPipeline())
                        .build(),
                "Fetched job indexing strategies for site ID: " + siteId
        );
    }

    @PostMapping("/validate-strategy")
    public ApiResponse validateJobIndexStrategy(
            @RequestBody JobIndexStrategySaveRequestDto strategy,
            @RequestParam(name = "site_id") Long siteId
    ) {
        List<SimpleJobModel> foundJobs = jobIndexUseCase.validateStrategyAndGetJobs(siteId, strategy.getProcessFlow());
        return new ApiResponse(
                true,
                foundJobs,
                "Validated " + foundJobs.size() + " jobs with the provided strategy"
        );
    }
}
