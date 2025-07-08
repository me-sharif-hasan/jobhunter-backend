package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetRenderedHtmlPageUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminSiteValidationService implements GetRenderedHtmlPageUseCase {
    private AdminSiteValidationDataAdapter adminSiteValidationDataAdapter;
    @Override
    public void getSiteAttributes(SiteAttributeValidatorModel siteAttributes) {
        System.out.println("Running scripts for site: " + siteAttributes.getUrl());
        adminSiteValidationDataAdapter.executeProcessFlow(siteAttributes.getUrl(), siteAttributes.getProcessFlow(), new JobIndexingService.OnJobAvailableCallback() {
            @Override
            public void onJobAvailable(SimpleJobModel jobModel) {

            }
        });
    }
}
