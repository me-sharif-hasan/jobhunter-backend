package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleNotificationModel;

import java.util.List;

public interface GetPersonalInAppNotificationsUseCase {
    List<SimpleNotificationModel> getNotifications(int page, int limit);
}
