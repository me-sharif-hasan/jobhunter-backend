package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.usecase.FavouriteJobUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetUserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/interests")
@AllArgsConstructor
public class InterestController {
    FavouriteJobUseCase favouriteJobUseCase;
    @GetMapping("/add-job")
    public String addFavouriteJob(
            @RequestParam(name = "job_id") String jobId
    )
    {
        if (jobId == null || jobId.isEmpty()) {
            throw new IllegalArgumentException("Job ID cannot be null or empty");
        }
        favouriteJobUseCase.addToFavourites(jobId);
        return "Job with ID: " + jobId + " added to favourites.";
    }
}
