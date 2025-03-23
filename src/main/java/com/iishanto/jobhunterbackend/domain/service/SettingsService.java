package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.SettingsDataAdapter;
import com.iishanto.jobhunterbackend.domain.usecase.SaveDeviceNotificationTokenUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SettingsService implements SaveDeviceNotificationTokenUseCase {
    SettingsDataAdapter settingsDataAdapter;
    @Override
    public void save(String token, String providerName) {
        settingsDataAdapter.saveNotificationToken(token,providerName);
    }
}
