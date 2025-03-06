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
}
