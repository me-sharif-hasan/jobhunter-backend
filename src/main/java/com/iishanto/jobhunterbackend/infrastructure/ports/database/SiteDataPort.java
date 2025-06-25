package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.UserJobAccessDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminSiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.exception.SiteAlreadyExistsException;
import com.iishanto.jobhunterbackend.infrastructure.crawler.WebCrawler;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.database.UserOwnedSite;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.domain.adapter.SiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserOwnedSiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SiteDataPort implements SiteDataAdapter, AdminSiteDataAdapter {
    private final WebCrawler webCrawler;
    private final UserJobAccessDataPort subscriptionDataPort;
    private final SiteRepository siteRepository;
    private final UserJobAccessDataAdapter userJobAccessDataAdapter;
    private final UserDataAdapter userDataAdapter;
    private final UserOwnedSiteRepository userOwnedSiteRepository;
//    public SiteDataPort(WebCrawler webCrawler, SiteRepository siteRepository){
//        this.webCrawler = webCrawler;
//        this.siteRepository=siteRepository;
//    }


    @Override
    public SimpleSiteModel getExistingSiteByJobListUrl(String jobListUrl) {
        Site site = siteRepository.findByJobListPageUrl(jobListUrl).orElseGet(()->{
            String refinedJobListPageUrl = jobListUrl;
            if(refinedJobListPageUrl.endsWith("/")) {
                refinedJobListPageUrl = refinedJobListPageUrl.substring(0, refinedJobListPageUrl.length() - 1);
            }else{
                refinedJobListPageUrl = refinedJobListPageUrl + "/";
            }
            Optional<Site> siteByHomePage = siteRepository.findByJobListPageUrl(refinedJobListPageUrl);
            return siteByHomePage.orElse(null);
        });

        return site==null?null:site.toDomain();
    }

    @Override
    public Long saveSite(SimpleSiteModel siteModel) {
        SimpleSiteModel existingSite = getExistingSiteByJobListUrl(siteModel.getJobListPageUrl());
        if (existingSite!=null) {
            throw new SiteAlreadyExistsException("Job list page URL already exists: " + siteModel.getJobListPageUrl(),existingSite.getId());
        }
        Site site=Site.fromSiteModel(siteModel);
        siteRepository.save(site);
        return site.getId();
    }

    @Override
    public String getRawHtml(String url) {
        return webCrawler.getRawHtml(url);
    }

    @Override
    public SimpleSiteModel getSiteByJobListUrl(String jobListUrl) {
        Optional<Site> site=siteRepository.findByJobListPageUrl(jobListUrl);
        return site.map(Site::toDomain).orElse(null);
    }

    @Override
    public SimpleSiteModel getSiteByHomePage(String homePage) {
        Optional<Site> site=siteRepository.findByHomepage(homePage);
        return site.map(Site::toDomain).orElse(null);
    }

    @Override
    public SimpleSiteModel getSite(Long Id) {
        Optional<Site> site=siteRepository.findById(Id);
        return site.map(Site::toDomain).orElse(null);
    }

    @Override
    public List<SimpleSiteModel> getSites(int page, int size,String query) {
        if(size>50) throw new IllegalArgumentException("Size can't be greater than 40");
        Pageable pageable= PageRequest.of(page,size);
        System.out.println("Page: "+page+" Size: "+size+" Query: "+query);
        List <Site> sites=siteRepository.findAllByNameContainingOrDescriptionContainingOrderByCreatedAtDesc(query,query,pageable);
        List <SimpleSiteModel> subscribedSites= new java.util.ArrayList<>(subscriptionDataPort.getSubscribedSitesInSideIds(
                userDataAdapter.getLoggedInUser().getId(),
                sites.stream().map(Site::getId).toList()
        ).stream().toList());
        Map<Long,Boolean> subscribedSitesMap=new HashMap<>();
        subscribedSites.forEach(simpleSiteModel -> subscribedSitesMap.put(simpleSiteModel.getId(),true));
        subscribedSites.sort((o1, o2) -> {
            if(o1.isSubscribed()&&o2.isSubscribed()) return 0;
            if(o1.isSubscribed()) return -1;
            return 1;
        });
        return sites.stream().map(Site::toDomain).peek(simpleSiteModel -> simpleSiteModel.setSubscribed(
                subscribedSitesMap.getOrDefault(simpleSiteModel.getId(),false)
        )).toList();
    }

    @Override
    public void addSiteOwner(SimpleSiteModel site, SimpleUserModel siteOwner) {
        UserOwnedSite userOwnedSite=new UserOwnedSite();
        userOwnedSite.setSite(Site.fromSiteModel(site));
        userOwnedSite.setUser(User.fromUserModel(siteOwner));
        try{
            userOwnedSiteRepository.save(userOwnedSite);
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to add site owner: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleSiteModel> getAllSitesForAdmin(int page, int limit, String query) {
        return siteRepository.findAllByNameContainingOrDescriptionContainingOrderByCreatedAtDesc(query,query,PageRequest.of(page,limit)).stream().map(Site::toDomain).toList();
    }

    @Override
    public long countAllSites() {
        return siteRepository.count();
    }
}
