package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.SubscriptionDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSubscriptionUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetSubscribedJobsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscribedJobsService implements AddSubscriptionUseCase, GetSubscribedJobsUseCase {
    private final UserDataAdapter userDataAdapter;
    private final SubscriptionDataAdapter subscriptionDataAdapter;
    @Override
    public Long createSubscription(SimpleSubscriptionModel subscriptionModel) {
        SimpleUserModel simpleUserModel=userDataAdapter.getLoggedInUser();
        subscriptionModel.setUser(simpleUserModel);
        return subscriptionDataAdapter.addSubscription(subscriptionModel);
    }

    @Override
    public void removeSubscription(SimpleSubscriptionModel simpleSubscriptionModel) {
        SimpleUserModel simpleUserModel=userDataAdapter.getLoggedInUser();
        System.out.println("Removing subscription: "+simpleSubscriptionModel+" "+simpleUserModel);
        simpleSubscriptionModel.setUser(simpleUserModel);
        subscriptionDataAdapter.removeSubscription(simpleSubscriptionModel);
    }

    @Override
    public List<SimpleJobModel> getSubscribedJobs() {
        return subscriptionDataAdapter.getSubscribedJobsOf(
                userDataAdapter.getLoggedInUser().getId()
        );
    }

    @Override
    public List<SimpleJobModel> getSubscribedJobs(int page, int limit, String query,int siteId) {
        if(page<0){
            throw new IllegalArgumentException("Page can not be less than zero");
        }
        if(limit>50){
            throw new IllegalArgumentException("Too high limit, max is 50");
        }
        if (query!=null&&query.length()>25){
            throw new IllegalArgumentException("Too long query");
        }
        if(siteId==-1){
            return subscriptionDataAdapter.getSubscribedJobsOf(
                    userDataAdapter.getLoggedInUser().getId(),
                    page,
                    limit,
                    query
            );
        }else{
            System.out.println("SITEIDii: "+siteId);
            return subscriptionDataAdapter.getSubscribedJobsOf(
                    userDataAdapter.getLoggedInUser().getId(),
                    page,
                    limit,
                    query,
                    siteId
            );
        }
    }
}
