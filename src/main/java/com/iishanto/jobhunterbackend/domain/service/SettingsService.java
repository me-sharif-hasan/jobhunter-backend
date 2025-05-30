package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.SettingsDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import com.iishanto.jobhunterbackend.domain.usecase.GetAppliedOptionsDatasource;
import com.iishanto.jobhunterbackend.domain.usecase.SaveDeviceNotificationTokenUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SettingsService implements SaveDeviceNotificationTokenUseCase, GetAppliedOptionsDatasource {
    SettingsDataAdapter settingsDataAdapter;
    @Override
    public void save(String token, String providerName) {
        settingsDataAdapter.saveNotificationToken(token,providerName);
    }

    @Override
    public List<String> getAppliedOptions() {
        return Stream.of(JobApplicationStatus.values()).map(Enum::name).toList();
    }
}
