package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.service.SiteService;
import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSitesUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.UpdateSiteUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.UpdateSiteDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/site")
@AllArgsConstructor
public class AdminSiteController {
    private GetAllSiteUseCase getAllSiteUseCase;
    private final AddSiteUseCase addSiteUseCase;
    private final UpdateSiteUseCase updateSiteUseCase;

    @GetMapping
    public ApiResponse getAllSite(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ){
        ApiResponse apiResponse=new ApiResponse(
                true,
                getAllSiteUseCase.getAllSites(page,limit,query),
                "Sites fetched successfully"
        );
        long numberOfTotalSites = getAllSiteUseCase.getTotalSitesCount();
        apiResponse.setTotalRecords(numberOfTotalSites);
        return apiResponse;
    }

    @PostMapping
    public ApiResponse addSite(
            @Valid @RequestBody UpdateSiteDto site
    ){
        Long ID = addSiteUseCase.addSite(site.getHomepage(), site.getJobListPageUrl());
        return new ApiResponse(true, ID, "Site added successfully");
    }

    @PatchMapping("/{siteId}")
    public ApiResponse updateSite(
            @PathVariable Long siteId,
            @Valid @RequestBody UpdateSiteDto site
    ){
        updateSiteUseCase.updateSite(siteId, site.toDomain());
        return new ApiResponse(true, siteId, "Site updated successfully");
    }
}
