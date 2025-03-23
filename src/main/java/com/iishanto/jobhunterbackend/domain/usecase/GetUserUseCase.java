package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

public interface GetUserUseCase {
    SimpleUserModel getCurrentUser();
    SimpleUserModel getUserByEmail(String email);
}
