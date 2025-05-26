package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;

import java.util.List;

public interface UserJobAccessDataAdapter {
    Long addSubscription(SimpleSubscriptionModel subscriptionModel);
    List<SimpleJobModel> getSubscribedJobsOf(Long userId);
    List<SimpleJobModel> getSubscribedJobsOf(Long userId,int page,int limit,String query);
    List<SimpleJobModel> getSubscribedJobsOf(Long userId,int page,int limit,String query,int siteId);
    List<SimpleSiteModel> getSubscribedSitesInSideIds(Long userId,List<Long> siteIds);

    void removeSubscription(SimpleSubscriptionModel simpleSubscriptionModel);

    List<SimpleJobModel> getAppliedJobs(int page, int limit, String query,int siteId,Long userId);

    List<SimpleJobModel> getAllJobs(int page, int limit, String query, Long userId, Long siteId);
}
