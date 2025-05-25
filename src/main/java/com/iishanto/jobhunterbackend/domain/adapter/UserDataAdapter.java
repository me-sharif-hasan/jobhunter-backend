package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

import java.util.List;

public interface UserDataAdapter {
    Long addUser(SimpleUserModel user);
    SimpleUserModel getLoggedInUser();
    SimpleUserModel getUserById(Long id);
    SimpleUserModel getUserByEmail(String email);

}
