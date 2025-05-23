package com.iishanto.jobhunterbackend.domain.usecase;

public interface FavouriteJobUseCase {
    void addToFavourites(String jobId);

    void removeFromFavourites(String jobId);

    boolean isJobInFavourites(String jobId);
}
