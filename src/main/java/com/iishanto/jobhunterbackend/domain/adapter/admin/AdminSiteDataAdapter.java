package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

import java.util.List;

public interface AdminSiteDataAdapter {
    List<SimpleSiteModel> getAllSitesForAdmin(int page, int limit, String query);

    long countAllSites();
}
