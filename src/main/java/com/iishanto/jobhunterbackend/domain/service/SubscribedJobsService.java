package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.UserJobAccessDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSubscriptionUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.UserJobAccessUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscribedJobsService implements AddSubscriptionUseCase, UserJobAccessUseCase {
    private final UserDataAdapter userDataAdapter;
    private final UserJobAccessDataAdapter userJobAccessDataAdapter;
    @Override
    public Long createSubscription(SimpleSubscriptionModel subscriptionModel) {
        SimpleUserModel simpleUserModel=userDataAdapter.getLoggedInUser();
        subscriptionModel.setUser(simpleUserModel);
        return userJobAccessDataAdapter.addSubscription(subscriptionModel);
    }

    @Override
    public void removeSubscription(SimpleSubscriptionModel simpleSubscriptionModel) {
        SimpleUserModel simpleUserModel=userDataAdapter.getLoggedInUser();
        System.out.println("Removing subscription: "+simpleSubscriptionModel+" "+simpleUserModel);
        simpleSubscriptionModel.setUser(simpleUserModel);
        userJobAccessDataAdapter.removeSubscription(simpleSubscriptionModel);
    }

    @Override
    public List<SimpleJobModel> getSubscribedJobs() {
        return userJobAccessDataAdapter.getSubscribedJobsOf(
                userDataAdapter.getLoggedInUser().getId()
        );
    }


    @Override
    public List<SimpleJobModel> getSubscribedJobs(int page, int limit, String query,int siteId) {
        validateRequest(page, limit, query);
        List<SimpleJobModel> simpleJobModels;
        Long userId = userDataAdapter.getLoggedInUser().getId();
        if(siteId==-1){
            simpleJobModels = userJobAccessDataAdapter.getSubscribedJobsOf(
                    userId,
                    page,
                    limit,
                    query
            );
        }else{
            System.out.println("SITEIDii: "+siteId);
            simpleJobModels = userJobAccessDataAdapter.getSubscribedJobsOf(
                    userId,
                    page,
                    limit,
                    query,
                    siteId
            );
        }

        return simpleJobModels;
    }

    @Override
    public List<SimpleJobModel> getAppliedJobs(int page, int limit, String query, int siteId) {
        validateRequest(page, limit, query);
        Long userId = userDataAdapter.getLoggedInUser().getId();
        return userJobAccessDataAdapter.getAppliedJobs(
                page,
                limit,
                query,
                siteId,
                userId
        );
    }

    @Override
    public List<SimpleJobModel> getAllJobs(int page, int limit, String query, int siteId) {
        Long userId = userDataAdapter.getLoggedInUser().getId();
        return userJobAccessDataAdapter.getAllJobs(page,limit,query,userId);
    }

    private static void validateRequest(int page, int limit, String query) {
        if(page <0){
            throw new IllegalArgumentException("Page can not be less than zero");
        }
        if(limit >50){
            throw new IllegalArgumentException("Too high limit, max is 50");
        }
        if (query !=null&& query.length()>25){
            throw new IllegalArgumentException("Too long query");
        }
    }
}
