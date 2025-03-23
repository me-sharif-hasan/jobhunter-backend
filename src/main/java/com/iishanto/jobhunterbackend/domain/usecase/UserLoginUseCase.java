package com.iishanto.jobhunterbackend.domain.usecase;

public interface UserLoginUseCase {
    String loginByGoogleTokenAndEmail(String email,String googleToken);
}
