package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

public interface UpdateSiteUseCase {
    void updateSite(Long siteId, SimpleSiteModel site);
}
