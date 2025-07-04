package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

public interface AddUserUseCase {
    Long addUser(SimpleUserModel user);
    Long addUserFromGoogle(SimpleUserModel user);
    SimpleUserModel authorizeUsingGoogleToken(String googleToken);
}
