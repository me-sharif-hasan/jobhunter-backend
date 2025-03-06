package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.SubscriptionDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.database.Subscription;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SubscriptionRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SubscriptionDataPort implements SubscriptionDataAdapter {
    private final SubscriptionRepository subscriptionRepository;
    private final SiteRepository siteRepository;
    private final UserRepository userRepository;
    private final JobsRepository jobsRepository;
    @Override
    public Long addSubscription(SimpleSubscriptionModel subscriptionModel) {
        subscriptionModel.setActive(true);
        if(subscriptionRepository.findFirstByUserIdAndSiteId(subscriptionModel.getUser().getId(),subscriptionModel.getSite().getId()).isPresent()){
            throw new RuntimeException("Already Subscribed");
        }
        Subscription subscription=Subscription.fromSubscriptionModels(userRepository,siteRepository,subscriptionModel);
        System.out.println("SUBSCRIPTIONxx: "+subscription+" "+subscriptionModel);
        subscriptionRepository.save(subscription);
        return subscription.getId();
    }

    @Override
    public List<SimpleJobModel> getSubscribedJobsOf(Long userId) {
        List <Subscription> subscriptions=subscriptionRepository.findAllByUserId(userId);
        List <Site> sites=subscriptions.stream().map(Subscription::getSite).toList();
        List <Long> siteIds=sites.stream().map(Site::getId).toList();
        List <Jobs> jobs = jobsRepository.findTopNBySiteIn(siteIds,70);
        System.out.println("REC: "+jobs+":"+jobs.size());
        return jobs.stream().map(Jobs::toSimpleJobModel).toList();
    }

    @Override
    public List<SimpleSiteModel> getSubscribedSitesInSideIds(Long userId, List<Long> siteIds) {
        List <Subscription> subscriptions=subscriptionRepository.findAllByUserIdAndSiteIdIn(userId,siteIds);
        return subscriptions.stream().map(Subscription::getSite).map(Site::toDomain).toList();
    }

    @Override
    public void removeSubscription(SimpleSubscriptionModel simpleSubscriptionModel) {
        Subscription subscription=subscriptionRepository.findFirstByUserIdAndSiteId(simpleSubscriptionModel.getUser().getId(),simpleSubscriptionModel.getSite().getId()).orElseThrow(()->new RuntimeException("Subscription not found"));
        subscriptionRepository.deleteById(subscription.getId());
        System.out.println("Deleted: "+subscription);
    }
}
