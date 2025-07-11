package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteIndexingStrategyCompositionModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.service.SiteService;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetSiteStrategyUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminSiteService implements GetAllSiteUseCase, GetSiteStrategyUseCase {
    AdminSiteDataAdapter siteService;

    @Override
    public List<SimpleSiteModel> getAllSites(int page, int limit, String query) {
        return siteService.getAllSitesForAdmin(page, limit, query);
    }

    @Override
    public long getTotalSitesCount() {
        return siteService.countAllSites();
    }

    @Override
    public SimpleSiteIndexingStrategyCompositionModel getSiteStrategy(Long siteId) {
        return siteService.getIndexingStrategy(siteId);
    }
}
