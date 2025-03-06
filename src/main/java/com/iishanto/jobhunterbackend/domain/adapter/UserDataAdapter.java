package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

public interface UserDataAdapter {
    Long addUser(SimpleUserModel user);
    SimpleUserModel getLoggedInUser();
}
