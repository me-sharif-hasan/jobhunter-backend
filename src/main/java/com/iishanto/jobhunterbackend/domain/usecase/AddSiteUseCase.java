package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

public interface AddSiteUseCase {
    public Long addSite(SimpleSiteModel site);
    public Long addSite(String url,String jobListPageUrl);
}
