package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.SiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSiteUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSitesUseCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


@Service
public class SiteService implements AddSiteUseCase, GetSiteUseCase, GetSitesUseCase {
    private final SiteDataAdapter siteDataAdapter;
    public SiteService(SiteDataAdapter siteDataAdapter){
        this.siteDataAdapter=siteDataAdapter;
    }
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
        Timestamp timestamp=new Timestamp(0);
        siteModel.setLastCrawledAt(timestamp);
        return siteDataAdapter.saveSite(
                siteModel.withJobListPageUrl(jobListPageUrl)
        );
    }

    public SimpleSiteModel getSiteInformationFromUrl(String url){
        String html=siteDataAdapter.getRawHtml(url);
        Document document= Jsoup.parse(html);
        String title=document.title();
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
        return null;
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
