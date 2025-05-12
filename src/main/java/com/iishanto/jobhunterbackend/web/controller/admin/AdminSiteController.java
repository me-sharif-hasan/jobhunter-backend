package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.service.SiteService;
import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSitesUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllSiteUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.AddSiteDto;
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
            @Valid @RequestBody AddSiteDto site
    ){
        Long ID = addSiteUseCase.addSite(site.homepage,site.jobListPageUrl);
        return new ApiResponse(true,ID,"Site added successfully");
    }
}
