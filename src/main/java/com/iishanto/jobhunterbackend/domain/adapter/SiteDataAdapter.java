package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

import java.util.List;

public interface SiteDataAdapter {
    Long saveSite(SimpleSiteModel site);
    void setSitePublishedStatus(boolean published, Long siteId);
    String getRawHtml(String url);
    SimpleSiteModel getSiteByJobListUrl(String jobListUrl);
    SimpleSiteModel getSiteByHomePage(String homePage);
    SimpleSiteModel getSite(Long Id);
    List<SimpleSiteModel> getSites(int page, int size, String query);

    SimpleSiteModel getExistingSiteByJobListUrl(String jobListUrl);
    void addSiteOwner(SimpleSiteModel site, SimpleUserModel siteOwner);

    List<SimpleSiteModel> getPersonalSites(int page, int size, String query,Long userId);
}
