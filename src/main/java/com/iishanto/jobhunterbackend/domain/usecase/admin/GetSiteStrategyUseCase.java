package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteIndexingStrategyCompositionModel;

public interface GetSiteStrategyUseCase {
    SimpleSiteIndexingStrategyCompositionModel getSiteStrategy(Long siteId);
}
