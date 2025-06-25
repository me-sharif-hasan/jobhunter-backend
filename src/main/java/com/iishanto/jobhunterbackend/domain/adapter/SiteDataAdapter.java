package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

import java.util.List;

public interface SiteDataAdapter {
    Long saveSite(SimpleSiteModel site);
    String getRawHtml(String url);
    public SimpleSiteModel getSiteByJobListUrl(String jobListUrl);
    public SimpleSiteModel getSiteByHomePage(String homePage);
    public SimpleSiteModel getSite(Long Id);
    public List<SimpleSiteModel> getSites(int page, int size,String query);

    SimpleSiteModel getExistingSiteByJobListUrl(String jobListUrl);
    void addSiteOwner(SimpleSiteModel site, SimpleUserModel siteOwner);
}
