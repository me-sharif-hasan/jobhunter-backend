package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;

public interface AddSubscriptionUseCase {
    Long createSubscription(SimpleSubscriptionModel subscriptionModel);

    void removeSubscription(SimpleSubscriptionModel simpleSubscriptionModel);
}
