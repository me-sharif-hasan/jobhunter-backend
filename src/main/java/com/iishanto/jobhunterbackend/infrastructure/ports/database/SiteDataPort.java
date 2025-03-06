package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.SubscriptionDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.infrastructure.crawler.WebCrawler;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.domain.adapter.SiteDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
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
public class SiteDataPort implements SiteDataAdapter {
    private final WebCrawler webCrawler;
    private final SubscriptionDataPort subscriptionDataPort;
    private final SiteRepository siteRepository;
    private final SubscriptionDataAdapter subscriptionDataAdapter;
    private final UserDataAdapter userDataAdapter;
//    public SiteDataPort(WebCrawler webCrawler, SiteRepository siteRepository){
//        this.webCrawler = webCrawler;
//        this.siteRepository=siteRepository;
//    }
    @Override
    public Long saveSite(SimpleSiteModel siteModel) {
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
    public List<SimpleSiteModel> getSites(int page, int size) {
        Pageable pageable= PageRequest.of(page,size);
        List <Site> sites=siteRepository.findAllByOrderByCreatedAtDesc(pageable);
        List <SimpleSiteModel> subscribedSites=subscriptionDataPort.getSubscribedSitesInSideIds(
                userDataAdapter.getLoggedInUser().getId(),
                sites.stream().map(Site::getId).toList()
        ).stream().toList();
        Map<Long,Boolean> subscribedSitesMap=new HashMap<>();
        subscribedSites.forEach(simpleSiteModel -> subscribedSitesMap.put(simpleSiteModel.getId(),true));
        return sites.stream().map(Site::toDomain).peek(simpleSiteModel -> simpleSiteModel.setSubscribed(
                subscribedSitesMap.getOrDefault(simpleSiteModel.getId(),false)
        )).toList();
    }
}
