package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddUserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements AddUserUseCase {
    UserDataAdapter userDataAdapter;
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
}
