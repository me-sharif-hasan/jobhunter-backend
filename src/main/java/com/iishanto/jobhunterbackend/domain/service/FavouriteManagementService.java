package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.FavouriteJobUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetUserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FavouriteManagementService implements FavouriteJobUseCase {
    GetUserUseCase getUserUseCase;
    @Override
    public void addToFavourites(String jobId) {
        SimpleUserModel simpleUserModel = getUserUseCase.getCurrentUser();
        if (simpleUserModel == null) {
            throw new RuntimeException("User not found");
        }

    }

    @Override
    public void removeFromFavourites(String jobId) {

    }

    @Override
    public boolean isJobInFavourites(String jobId) {
        return false;
    }
}
