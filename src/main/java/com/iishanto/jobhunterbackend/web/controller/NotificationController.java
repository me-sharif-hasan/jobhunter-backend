package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.GetPersonalInAppNotificationsUseCase;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final GetPersonalInAppNotificationsUseCase getPersonalInAppNotificationsUseCase;

    @GetMapping
    public ApiResponse getNotifications(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ){
        if(page<0) throw new IllegalArgumentException("Page must be greater than 0");
        if(limit<0||limit>100) throw new IllegalArgumentException("Limit must be between 0 and 100");

        return new ApiResponse(true,getPersonalInAppNotificationsUseCase.getNotifications(page,limit),"Notifications fetched successfully.");
    }
}
