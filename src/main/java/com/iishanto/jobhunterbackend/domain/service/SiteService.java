package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.SiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSitesUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.admin.JobIndexUseCase;
import com.iishanto.jobhunterbackend.exception.SiteAlreadyExistsException;
import com.iishanto.jobhunterbackend.exception.UserAlreadyOwnsSiteException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class SiteService implements AddSiteUseCase, GetSiteUseCase, GetSitesUseCase {
    private final SiteDataAdapter siteDataAdapter;
    private final UserDataAdapter userDataAdapter;
    private final JobIndexUseCase jobIndexUseCase;
    @Override
    public Long addSite(SimpleSiteModel site) {
        return siteDataAdapter.saveSite(site);
    }

    @Override
    public Long addSite(String url,String jobListPageUrl) {
        if(jobListPageUrl.isEmpty()){
            throw new IllegalArgumentException("Job list page url is required");
        }
        if(siteDataAdapter.getSiteByJobListUrl(jobListPageUrl)!=null){
            throw new IllegalArgumentException("Job list page url already exists");
        }
        SimpleSiteModel siteModel=getSiteInformationFromUrl(url);
        Long siteId = saveSite(siteModel, jobListPageUrl);
        jobIndexUseCase.createAiIndexingStrategy(siteId);
        return siteId;
    }

    private Long saveSite(SimpleSiteModel siteModel,String jobListPageUrl){
        Timestamp timestamp=new Timestamp(0);
        siteModel.setLastCrawledAt(timestamp);
        return siteDataAdapter.saveSite(
                siteModel.withJobListPageUrl(jobListPageUrl)
        );
    }

    @Override
    public SimpleSiteModel reviewSite(String siteCareerPageUrl, String siteHomepageUrl) {
        SimpleSiteModel site = getSiteInformationFromUrl(siteHomepageUrl);
        site.setJobListPageUrl(siteCareerPageUrl);
        return site;
    }

    @Override
    public SimpleSiteModel addPersonalSite(String jobListPageUrl, String homepage) {
        if(jobListPageUrl.isEmpty()){
            throw new IllegalArgumentException("Job list page url is required");
        }
        SimpleSiteModel site = getSiteInformationFromUrl(homepage);
        if(site == null){
            throw new IllegalArgumentException("Invalid homepage URL");
        }
        SimpleUserModel siteOwner=userDataAdapter.getLoggedInUser();
        if(siteOwner == null){
            throw new IllegalArgumentException("User not logged in");
        }
        Long siteId;
        try{
            siteId = saveSite(site, jobListPageUrl);
            siteDataAdapter.setSitePublishedStatus(false,siteId);
        }catch (SiteAlreadyExistsException e){
            siteId= e.getExistingSiteId();
            if(siteId==null) throw e;
        }
        site=siteDataAdapter.getSite(siteId);
        try{
            siteDataAdapter.addSiteOwner(site, siteOwner);
            return site;
        }catch (Exception e){
            throw new UserAlreadyOwnsSiteException(
                    "User already owns this site",
                    siteId,
                    siteOwner.getId()
            );
        }
    }

    public SimpleSiteModel getSiteInformationFromUrl(String url){
        String html=siteDataAdapter.getRawHtml(url);
        Document document= Jsoup.parse(html);
        String title=document.getElementsByTag("title").text();
        String description=document.select("meta[name=description]").attr("content");
        String icon=document.select("link[rel=icon]").attr("href");
        if(icon.isEmpty()){
            icon="/favicon.ico";
        }
        URI uri=URI.create(icon);
        if(uri.isAbsolute()){
            icon=uri.getPath();
        }
        if(!icon.startsWith("/")){
            icon="/"+icon;
        }
        return new SimpleSiteModel(
                null,
                title,
                url,
                description,
                icon,
                null,
                new Timestamp(Instant.now().toEpochMilli())
        );
    }

    @Override
    public SimpleSiteModel getSite(Long Id) {
        return null;
    }

    @Override
    public SimpleSiteModel getSiteByJobListUrl(String jobListUrl) {
        return siteDataAdapter.getExistingSiteByJobListUrl(jobListUrl);
    }

    @Override
    public List<SimpleSiteModel> getPersonalSites(int page, int size, String query) {
        Long userId = userDataAdapter.getLoggedInUser().getId();
        return siteDataAdapter.getPersonalSites(page, size, query,userId);
    }

    @Override
    public SimpleSiteModel getSiteByHomePage(String homePage) {
        return null;
    }

    @Override
    public List<SimpleSiteModel> getSites(int page, int size,String query) {
        return siteDataAdapter.getSites(page,size,query);
    }
}
