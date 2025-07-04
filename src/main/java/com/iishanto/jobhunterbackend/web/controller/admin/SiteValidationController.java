package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetRenderedHtmlPageUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.SiteValidationDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/site-validation")
@AllArgsConstructor
public class SiteValidationController {
    private final GetRenderedHtmlPageUseCase getRenderedHtmlPageUseCase;
    @PostMapping
    public ApiResponse getRenderedPage(@RequestBody SiteValidationDto site) {
        // This method should implement the logic to get a rendered page screenshot
        // For now, we return a placeholder response
        getRenderedHtmlPageUseCase.getSiteAttributes(
                SiteAttributeValidatorModel.builder()
                        .url(site.getUrl())
                        .processFlow(site.getProcessFlow())
                        .build()
        );
        return null;
    }
}

