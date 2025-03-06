package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;

import java.util.List;

public interface SubscriptionDataAdapter {
    Long addSubscription(SimpleSubscriptionModel subscriptionModel);
    List<SimpleJobModel> getSubscribedJobsOf(Long userId);
    List<SimpleSiteModel> getSubscribedSitesInSideIds(Long userId,List<Long> siteIds);

    void removeSubscription(SimpleSubscriptionModel simpleSubscriptionModel);
}
