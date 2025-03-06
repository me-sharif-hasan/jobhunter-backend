package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;
    private Timestamp lastLogout;
    private String lastIp;

//    @OneToMany(mappedBy = "user")
//    private List<Subscription> subscriptions;

    public static User fromUserModel(SimpleUserModel userModel){
        User user=new User();
        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        user.setPassword(userModel.getPassword());
        user.setRole(userModel.getRole());
        user.setCreatedAt(userModel.getCreatedAt());
        user.setUpdatedAt(userModel.getUpdatedAt());
        user.setLastLogin(userModel.getLastLogin());
        user.setLastLogout(userModel.getLastLogout());
        user.setLastIp(userModel.getLastIp());
//        user.setSubscriptions(
//                userModel.getSubscriptions().stream().map(Subscription::fromSubscriptionModels).toList()
//        );
        return user;
    }

    public SimpleUserModel toUserModel() {
        return SimpleUserModel.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .lastLogin(lastLogin)
                .lastLogout(lastLogout)
                .lastIp(lastIp)
//                .subscriptions(
//                        subscriptions.stream().map(Subscription::toSubscriptionModel).toList()
//                )
                .build();
    }
}
