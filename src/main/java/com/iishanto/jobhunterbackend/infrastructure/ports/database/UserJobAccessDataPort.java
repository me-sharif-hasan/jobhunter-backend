package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.UserJobAccessDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.database.Subscription;
import com.iishanto.jobhunterbackend.infrastructure.projection.PersonalJobProjection;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SubscriptionRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserJobAccessDataPort implements UserJobAccessDataAdapter {
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
        return getSubscribedJobsOf(userId,0,500,"");
    }

    @Override
    public List<SimpleJobModel> getSubscribedJobsOf(Long userId, int page, int limit, String query) {
        List <Site> sites=getSubscribedSites(userId);
        List <Long> siteIds=sites.stream().map(Site::getId).toList();
        return getSimpleJobModels(userId, page, limit, query, siteIds);
    }

    private List<Site> getSubscribedSites(Long userId){
        System.out.println("USERID: "+userId);
        List <Subscription> subscriptions=subscriptionRepository.findAllByUserId(userId);
        System.out.println("SUBS: "+subscriptions+":"+subscriptions.size());
        return subscriptions.stream().map(Subscription::getSite).toList();
    }

    @Override
    public List<SimpleJobModel> getSubscribedJobsOf(Long userId, int page, int limit, String query, int siteId) {
        List <Site> sites=getSubscribedSites(userId);
        List <Long> siteIds=sites.stream().map(Site::getId).filter(
                id -> siteId < 0 || id == siteId
        ).toList();
        return getSimpleJobModels(userId, page, limit, query, siteIds);
    }

    @NotNull
    private List<SimpleJobModel> getSimpleJobModels(Long userId, int page, int limit, String query, List<Long> siteIds) {
        Pageable pageable= PageRequest.of(page,limit);
        System.out.println(page+" "+limit);
        List<PersonalJobProjection> personalJobProjections = jobsRepository.findJobs(siteIds,userId,query,pageable)
                .getContent();
        return personalJobProjections.stream().map(projection -> {
            SimpleJobModel jobModel = Jobs.fromProjection(projection).toSimpleJobModel();
            jobModel.setApplied(projection.getIsApplied()!=null&&projection.getIsApplied());
            return jobModel;
        }).toList();
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

    @Override
    public List<SimpleJobModel> getAppliedJobs(int page, int limit, String query, int siteId, Long userId) {
        Pageable pageable= PageRequest.of(page,limit);
        if(siteId>=0){
            List<Long> siteIds=List.of((long)siteId);
            List<SimpleJobModel> sjm = jobsRepository.findAppliedJobs(
                    siteIds,
                    userId,
                    query,
                    pageable
            ).stream().map(personalJobProjection -> {
                SimpleJobModel jobModel = Jobs.fromProjection(personalJobProjection).toSimpleJobModel();
                jobModel.setApplied(personalJobProjection.getIsApplied()!=null&&personalJobProjection.getIsApplied());
                jobModel.setAppliedAt(personalJobProjection.getAppliedAt());
                jobModel.setApplicationStatus(personalJobProjection.getApplicationStatus());
                return jobModel;
            }).toList();

            return sjm;
        }else{
            return jobsRepository.findAppliedJobs(
                    userId,
                    query,
                    pageable
            ).stream().map(personalJobProjection -> {
                SimpleJobModel jobModel = Jobs.fromProjection(personalJobProjection).toSimpleJobModel();
                jobModel.setApplied(personalJobProjection.getIsApplied()!=null&&personalJobProjection.getIsApplied());
                jobModel.setAppliedAt(personalJobProjection.getAppliedAt());
                jobModel.setApplicationStatus(personalJobProjection.getApplicationStatus());
                return jobModel;
            }).toList();
        }
    }

    @Override
    public List <SimpleJobModel> getAllJobs(int page, int limit, String query, Long userId, Long siteId) {
        Pageable pageable= PageRequest.of(page,limit);
        List <PersonalJobProjection> projectedJobs = jobsRepository.findAllJobs(query, userId,siteId, pageable);
        return projectedJobs.stream().map(projectedJob->{
            SimpleJobModel jobModel = Jobs.fromProjection(projectedJob).toSimpleJobModel();
            jobModel.setApplied(projectedJob.getIsApplied()!=null&&projectedJob.getIsApplied());
            return jobModel;
        }).toList();
    }


}
