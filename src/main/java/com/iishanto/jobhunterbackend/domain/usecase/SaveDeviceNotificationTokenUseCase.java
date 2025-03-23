package com.iishanto.jobhunterbackend.domain.usecase;

public interface SaveDeviceNotificationTokenUseCase {
    void save(String token,String providerName);
}
