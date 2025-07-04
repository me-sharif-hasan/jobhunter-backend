package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;

public interface GetRenderedHtmlPageUseCase {
    void getSiteAttributes(SiteAttributeValidatorModel siteAttributes);
}
