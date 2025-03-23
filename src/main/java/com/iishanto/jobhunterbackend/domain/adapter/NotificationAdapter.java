package com.iishanto.jobhunterbackend.domain.adapter;

import java.util.List;

public interface NotificationAdapter {
    void sendJobNotification(List<String> jobIds);
}
