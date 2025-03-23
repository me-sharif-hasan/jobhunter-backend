package com.iishanto.jobhunterbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SimpleUserModel {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String token;
    private String imageUrl;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;
    private Timestamp lastLogout;
    private String lastIp;
    private List<SimpleSubscriptionModel> subscriptions;
}
