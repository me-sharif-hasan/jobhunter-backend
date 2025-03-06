package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSitesUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.AddSiteDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site")
@AllArgsConstructor
public class SiteController {
    private final AddSiteUseCase addSiteUseCase;
    private final GetSitesUseCase getSitesUseCase;
    @PostMapping
    public ApiResponse addSite(
            @Valid @RequestBody AddSiteDto site
    ){
        Long ID = addSiteUseCase.addSite(site.homepage,site.jobListPageUrl);
        return new ApiResponse(true,ID,"Site added successfully");
    }

    @GetMapping
    public ApiResponse getSites(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        if (page<0 || size<1){
            throw new IllegalArgumentException("Invalid page or size");
        }
        if(size>40){
            throw new IllegalArgumentException("Size can't be greater than 100");
        }
        return new ApiResponse(true, getSitesUseCase.getSites(page,size),"Sites fetched successfully");
    }
}
