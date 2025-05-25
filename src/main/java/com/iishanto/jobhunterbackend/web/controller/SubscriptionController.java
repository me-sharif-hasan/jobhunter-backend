package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSubscriptionUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.UserJobAccessUseCase;
import com.iishanto.jobhunterbackend.web.dto.request.SubscriptionDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {
    private final AddSubscriptionUseCase addSubscriptionUseCase;
    private final UserJobAccessUseCase userJobAccessUseCase;
    @PostMapping
    public ApiResponse subscribe(@Valid @RequestBody SubscriptionDto subscriptionDto){
        System.out.println("Subscribed to site id: "+ subscriptionDto.getSite_id()+"\n");
        return new ApiResponse(
                true,
                addSubscriptionUseCase.createSubscription(subscriptionDto.toSimpleSubscriptionModel()),
                "Subscribed successfully"
        );
    }

    @GetMapping
    public ApiResponse getSubscriptions(){
        List<SimpleJobModel> subscriptions= userJobAccessUseCase.getSubscribedJobs();
        return new ApiResponse(true,subscriptions,"List of all subscribed jobs");
    }


    @PostMapping("/remove")
    public ApiResponse unsubscribe(@RequestBody SubscriptionDto subscriptionDto){
        addSubscriptionUseCase.removeSubscription(subscriptionDto.toSimpleSubscriptionModel());
        return new ApiResponse(true,subscriptionDto.getSite_id(),"Unsubscribed successfully");
    }
}
