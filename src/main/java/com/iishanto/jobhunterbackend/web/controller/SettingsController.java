package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.SaveDeviceNotificationTokenUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.FcmTokenSaveDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@AllArgsConstructor
public class SettingsController {
    SaveDeviceNotificationTokenUseCase saveDeviceNotificationTokenUseCase;
    public void connectWithFacebook(){}

    @PostMapping("/saveFirebaseToken")
    public void saveFirebaseToken(
            @RequestBody FcmTokenSaveDto fcmTokenSaveDto
    ){
        System.out.println("FCMTOKEN:"+fcmTokenSaveDto.getFcmToken());
        saveDeviceNotificationTokenUseCase.save(fcmTokenSaveDto.getFcmToken(),"firebase");
    }
}
