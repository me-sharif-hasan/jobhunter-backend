package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteValidationDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetRenderedHtmlPageUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminSiteValidationService implements GetRenderedHtmlPageUseCase {
    private AdminSiteValidationDataAdapter adminSiteValidationDataAdapter;
    @Override
    public String getRenderedHtmlPage(String url) {
        return adminSiteValidationDataAdapter.getRenderedHtmlPage(url);
    }

    @Override
    public void getSiteAttributes(SiteAttributeValidatorModel siteAttributes) {

    }
}
