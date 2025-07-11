package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteIndexingStrategyCompositionModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;

import java.util.List;
import java.util.Optional;

public interface AdminSiteDataAdapter {
    List<SimpleSiteModel> getAllSitesForAdmin(int page, int limit, String query);
    long countAllSites();
    List<SimpleSiteModel> getSitesForIndexing();
    Optional<SimpleSiteModel> getSiteById(Long siteId);
    Long saveIndexingStrategy(IndexingStrategyNames type, Long siteId, String jsonStrategy);
    SimpleSiteIndexingStrategyCompositionModel getIndexingStrategy(Long siteId);
}
