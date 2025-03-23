package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@Component
@AllArgsConstructor
public class UserDataPort implements UserDataAdapter {
    private final UserRepository userRepository;
    @Override
    public Long addUser(SimpleUserModel userModel) {
        System.out.println("User Added: "+userModel.toString());
        userModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userModel.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        User user=User.fromUserModel(userModel);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public SimpleUserModel getLoggedInUser() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email=user.getUsername();
        User dbUser=userRepository.findByEmail(email);
        return dbUser.toUserModel();
    }

    @Override
    public SimpleUserModel getUserByEmail(String email) {
        User user=userRepository.findByEmail(email);
        if(user==null) return null;
        return user.toUserModel();
    }
}
