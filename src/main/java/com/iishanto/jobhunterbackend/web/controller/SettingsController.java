package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.GetAppliedOptionsDatasource;
import com.iishanto.jobhunterbackend.domain.usecase.SaveDeviceNotificationTokenUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.FcmTokenSaveDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
@AllArgsConstructor
public class SettingsController {
    SaveDeviceNotificationTokenUseCase saveDeviceNotificationTokenUseCase;
    GetAppliedOptionsDatasource getAppliedOptionsDatasource;
    public void connectWithFacebook(){}

    @PostMapping("/saveFirebaseToken")
    public void saveFirebaseToken(
            @RequestBody FcmTokenSaveDto fcmTokenSaveDto
    ){
        System.out.println("FCMTOKEN:"+fcmTokenSaveDto.getFcmToken());
        saveDeviceNotificationTokenUseCase.save(fcmTokenSaveDto.getFcmToken(),"firebase");
    }

    @GetMapping("/job-applied-options")
    public ApiResponse getJobAppliedOptions(){
        List<String> options = getAppliedOptionsDatasource.getAppliedOptions();
        return new ApiResponse(true, options, "Job applied options fetched successfully.");
    }
}
