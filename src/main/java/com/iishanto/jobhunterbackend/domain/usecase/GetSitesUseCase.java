package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface GetSitesUseCase {
    public List<SimpleSiteModel> getSites(int page, int size,String query);

    SimpleSiteModel getSiteByJobListUrl(@NotBlank String jobListPageUrl);

    List<SimpleSiteModel> getPersonalSites(int page, int size, String query);
}
