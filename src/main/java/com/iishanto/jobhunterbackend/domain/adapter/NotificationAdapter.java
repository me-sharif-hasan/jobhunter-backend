package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleNotificationModel;

import java.util.List;

public interface NotificationAdapter {
    void sendJobNotification(List<String> jobIds);
    List<SimpleNotificationModel> getInAppNotification(int page, int limit);
}
