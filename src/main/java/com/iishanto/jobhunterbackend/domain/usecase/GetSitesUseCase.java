package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

import java.util.List;

public interface GetSitesUseCase {
    public List<SimpleSiteModel> getSites(int page, int size,String query);
}
