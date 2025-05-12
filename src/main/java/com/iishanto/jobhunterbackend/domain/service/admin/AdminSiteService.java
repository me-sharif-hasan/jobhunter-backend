package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.service.SiteService;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllSiteUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminSiteService implements GetAllSiteUseCase {
    AdminSiteDataAdapter siteService;

    @Override
    public List<SimpleSiteModel> getAllSites(int page, int limit, String query) {
        return siteService.getAllSitesForAdmin(page, limit, query);
    }

    @Override
    public long getTotalSitesCount() {
        return siteService.countAllSites();
    }
}
