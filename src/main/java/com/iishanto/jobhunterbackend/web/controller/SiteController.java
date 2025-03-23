package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSitesUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.AddSiteDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import com.iishanto.jobhunterbackend.web.dto.response.site.SiteResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse getSites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String query
    ){
        if (page<0 || size<1){
            throw new IllegalArgumentException("Invalid page or size");
        }
        if(size>40){
            throw new IllegalArgumentException("Size can't be greater than 100");
        }
        System.out.println("iiPage: "+page+" Size: "+size);
        List<SimpleSiteModel> sites = getSitesUseCase.getSites(page,size,query);
        System.out.println("iiSites: "+sites.size()+" "+page+" "+size);
        if(sites.isEmpty()){
            return new ApiResponse(false,null,"No sites found");
        }else{
            List<SiteResponseDto> siteResponseDtos = sites.stream().map(SiteResponseDto::fromSimpleSiteModel).toList();
            return new ApiResponse(true, siteResponseDtos,"Sites fetched successfully");

        }
    }
}
