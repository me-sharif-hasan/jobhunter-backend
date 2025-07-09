package com.iishanto.jobhunterbackend.infrastructure.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String imageUrl;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;
    private Timestamp lastLogout;
    private String lastIp;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
            name = "user_applied_jobs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    @JsonManagedReference
    private List<Opportunity> appliedJobs;

    public static User fromUserModel(SimpleUserModel userModel){
        return fromUserModel(userModel, null);
    }
    public static User fromUserModel(SimpleUserModel userModel,User existingUser) {
        User user=existingUser==null?new User():existingUser;
        if (userModel.getId()!=null){
            user.setId(userModel.getId());
        }
        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        user.setPassword(userModel.getPassword());
        user.setRole(userModel.getRole());
        user.setCreatedAt(userModel.getCreatedAt());
        user.setUpdatedAt(userModel.getUpdatedAt());
        user.setLastLogin(userModel.getLastLogin());
        user.setLastLogout(userModel.getLastLogout());
        user.setLastIp(userModel.getLastIp());
        user.imageUrl=userModel.getImageUrl();
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
                .imageUrl(imageUrl)
                .lastIp(lastIp)
                .build();
    }
}
