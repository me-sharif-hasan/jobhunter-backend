package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserAppliedJobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDataPort implements UserDataAdapter {
    private final UserRepository userRepository;
    UserAppliedJobsRepository userAppliedJobsRepository;
    @Override
    public Long addUser(SimpleUserModel userModel) {
        System.out.println("User Added: "+userModel.toString());
        User dbUser;
        if (userModel.getId() != null) {
            dbUser = userRepository.findById(userModel.getId()).orElse(new User());
        } else {
            dbUser = new User();
        }
        userModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userModel.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        User user=User.fromUserModel(userModel,dbUser);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public SimpleUserModel getLoggedInUser() {
        String email = getCurrentUserEmail();
        User dbUser=userRepository.findByEmail(email);
        return dbUser.toUserModel();
    }

    private static String getCurrentUserEmail() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    @Override
    public SimpleUserModel getUserById(Long id) {
        Optional<User> user=userRepository.findById(id);
        return user.map(User::toUserModel).orElse(null);
    }

    @Override
    public SimpleUserModel getUserByEmail(String email) {
        User user=userRepository.findByEmail(email);
        if(user==null) return null;
        return user.toUserModel();
    }
}
