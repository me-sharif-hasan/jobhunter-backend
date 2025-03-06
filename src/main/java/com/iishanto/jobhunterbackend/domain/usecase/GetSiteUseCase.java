package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

public interface GetSiteUseCase {
    public SimpleSiteModel getSite(Long Id);
    public SimpleSiteModel getSiteByJobListUrl(String jobListUrl);
    public SimpleSiteModel getSiteByHomePage(String homePage);
}
