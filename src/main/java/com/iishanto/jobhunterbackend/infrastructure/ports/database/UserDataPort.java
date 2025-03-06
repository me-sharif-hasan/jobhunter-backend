package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@AllArgsConstructor
public class UserDataPort implements UserDataAdapter {
    private final UserRepository userRepository;
    @Override
    public Long addUser(SimpleUserModel userModel) {
        userModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userModel.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        User user=User.fromUserModel(userModel);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public SimpleUserModel getLoggedInUser() {
        List<User> users=userRepository.findAll();
        return users.get(0).toUserModel();
    }
}
