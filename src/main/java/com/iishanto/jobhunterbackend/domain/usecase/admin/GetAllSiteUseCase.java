package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

import java.util.List;

public interface GetAllSiteUseCase {
    List<SimpleSiteModel> getAllSites(int page,int pageSize,String query);

    long getTotalSitesCount();
}
