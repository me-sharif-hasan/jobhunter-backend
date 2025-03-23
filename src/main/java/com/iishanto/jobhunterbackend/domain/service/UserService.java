package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.config.security.JwtUtil;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddUserUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.GetUserUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.UserLoginUseCase;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService implements AddUserUseCase, GetUserUseCase, UserLoginUseCase {
    UserDataAdapter userDataAdapter;
    JwtUtil jwtUtil;
    @Override
    public Long addUser(SimpleUserModel user) {
        if(user.getPassword()==null || user.getPassword().length()<8){
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if(user.getEmail()==null || !user.getEmail().contains("@")){
            throw new IllegalArgumentException("Invalid email address");
        }
        return userDataAdapter.addUser(user);
    }

    @Override
    public Long addUserFromGoogle(SimpleUserModel user) {
        if(user.getEmail()==null || !user.getEmail().contains("@")){
            throw new IllegalArgumentException("Invalid email address");
        }
        if(user.getName()==null || StringUtils.isBlank(user.getName())){
            throw new IllegalArgumentException("Name can't be empty");
        }
        String googleToken=user.getToken();
        if(Objects.isNull(googleToken) || googleToken.length()<10){
            throw new IllegalArgumentException("Invalid token");
        }
        SimpleUserModel simpleUserModel = userDataAdapter.getUserByEmail(user.getEmail());
        if(simpleUserModel!=null){
            user.setId(simpleUserModel.getId());
        }
        return userDataAdapter.addUser(user);
    }

    @Override
    public SimpleUserModel getCurrentUser() {
        return userDataAdapter.getLoggedInUser();
    }

    @Override
    public String loginByGoogleTokenAndEmail(String email, String googleToken) {
        return jwtUtil.generateToken(email,"ROLE_USER");
    }

    @Override
    public SimpleUserModel getUserByEmail(String email) {
        return userDataAdapter.getUserByEmail(email);
    }
}
