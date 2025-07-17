package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.database.UserResume;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserAppliedJobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserResumeRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDataPort implements UserDataAdapter {
    private final UserRepository userRepository;
    private final UserResumeRepository userResumeRepository;
    private final UserAppliedJobsRepository userAppliedJobsRepository;
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

    @Override
    public Long saveResume(String resumeContentAsText, Long userId) {
        UserResume userResume = new UserResume();
        userResume.setContent(resumeContentAsText);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        userResume.setUser(user);
        userResume = userResumeRepository.save(userResume);
        return Optional.ofNullable(userResume).orElseThrow(()->new RuntimeException("Save failure.")).getId();
    }

    @Override
    public String getResumeTextByUserId(Long userId) {
        UserResume userResume = userResumeRepository.findByUser_Id(userId);
        if (userResume == null) {
            throw new IllegalArgumentException("Resume not found for user ID: " + userId);
        }
        return userResume.getContent();
    }
}
