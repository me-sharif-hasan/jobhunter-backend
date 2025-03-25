package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.NotificationAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleNotificationModel;
import com.iishanto.jobhunterbackend.domain.usecase.GetPersonalInAppNotificationsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InAppJobService implements GetPersonalInAppNotificationsUseCase {
    private NotificationAdapter notificationAdapter;
    @Override
    public List<SimpleNotificationModel> getNotifications(int page, int limit) {
        return notificationAdapter.getInAppNotification(page,limit);
    }
}
