package com.iishanto.jobhunterbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.TimeZone;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSiteModel{
    private Long id;
    private String name;
    private String homepage;
    private String description;
    private String iconUrl;
    private String jobListPageUrl;
    private Timestamp lastCrawledAt;

    boolean isSubscribed=false;
    boolean isActive;
    public SimpleSiteModel (
            Long id,
            String name,
            String homepage,
            String description,
            String iconUrl,
            String jobListPageUrl,
            Timestamp lastCrawledAt
    ){
        this.id=id;
        this.name=name;
        this.homepage=homepage;
        this.description=description;
        this.iconUrl=iconUrl;
        this.jobListPageUrl=jobListPageUrl;
        this.lastCrawledAt=lastCrawledAt;
    }
    public SimpleSiteModel withJobListPageUrl(String jobListPageUrl){
        jobListPageUrl=jobListPageUrl.trim();
        if(jobListPageUrl.endsWith("/")){
            jobListPageUrl=jobListPageUrl.substring(0,jobListPageUrl.length()-1);
        }
        return new SimpleSiteModel(id,name,homepage,description,iconUrl,jobListPageUrl,lastCrawledAt);
    }
}