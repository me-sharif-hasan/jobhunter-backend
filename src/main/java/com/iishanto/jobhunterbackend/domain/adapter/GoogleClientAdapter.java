package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;

public interface GoogleClientAdapter {
    SimpleUserModel getUserFromIdToken(String idToken);
}
