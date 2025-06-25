package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import jakarta.validation.constraints.NotBlank;

public interface AddSiteUseCase {
    public Long addSite(SimpleSiteModel site);
    Long addSite(String url,String jobListPageUrl);
    SimpleSiteModel reviewSite(String siteCareerPageUrl, String siteHomepageUrl);

    SimpleSiteModel addPersonalSite(@NotBlank String jobListPageUrl, @NotBlank String homepage);
}
