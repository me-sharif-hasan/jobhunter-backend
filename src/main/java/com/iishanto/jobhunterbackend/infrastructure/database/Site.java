package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import com.iishanto.jobhunterbackend.domain.model.values.SiteControlStatus;
import com.iishanto.jobhunterbackend.domain.model.values.SiteRuntimeStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Data
@Entity
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String homepage;
    private String iconUrl;
    @Lob
    private String description;
    @Column(
            unique = true
    )
    private String jobListPageUrl;
    private Timestamp lastCrawledAt;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    private SiteControlStatus status = SiteControlStatus.ACTIVE;
    private SiteRuntimeStatus runtimeStatus = SiteRuntimeStatus.IDLE;

    private boolean isPublished = true;

    private IndexingStrategyNames indexingStrategy = IndexingStrategyNames.AI;


    public static Site fromSiteModel(SimpleSiteModel siteModel){
        Site site=new Site();
        site.setName(siteModel.getName());
        site.setId(siteModel.getId());
        site.setHomepage(siteModel.getHomepage());
        site.setJobListPageUrl(siteModel.getJobListPageUrl());
        site.setDescription(siteModel.getDescription());
        site.setIconUrl(siteModel.getIconUrl());
        site.setLastCrawledAt(siteModel.getLastCrawledAt());
        return site;
    }

    public SimpleSiteModel toDomain(){
        return SimpleSiteModel.builder()
                .description(description)
                .iconUrl(iconUrl)
                .lastCrawledAt(lastCrawledAt)
                .name(name)
                .homepage(homepage)
                .jobListPageUrl(jobListPageUrl)
                .id(id)
                .build();
    }
}
