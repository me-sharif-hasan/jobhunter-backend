package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;

import java.util.List;

public interface SiteDataAdapter {
    public Long saveSite(SimpleSiteModel site);
    public String getRawHtml(String url);
    public SimpleSiteModel getSiteByJobListUrl(String jobListUrl);
    public SimpleSiteModel getSiteByHomePage(String homePage);
    public SimpleSiteModel getSite(Long Id);
    public List<SimpleSiteModel> getSites(int page, int size);
}
