package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetRenderedHtmlPageUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AdminSiteValidationService implements GetRenderedHtmlPageUseCase {
    private AdminSiteValidationDataAdapter adminSiteValidationDataAdapter;
    @Override
    public void getSiteAttributes(SiteAttributeValidatorModel siteAttributes) {
        System.out.println("Running scripts for site: " + siteAttributes.getUrl());
        adminSiteValidationDataAdapter.runScript(siteAttributes.getUrl(), siteAttributes.getProcessFlow());
    }
}
