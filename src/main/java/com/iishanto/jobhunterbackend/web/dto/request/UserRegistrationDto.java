package com.iishanto.jobhunterbackend.web.dto.request;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
@Data
@Builder
@ToString
public class UserRegistrationDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private String password;
    private String confirmPassword;
    private String token;
    private String imageUrl;
    private String role;

    public SimpleUserModel toUserModel(){
        return SimpleUserModel.builder()
                .name(name)
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .role(role)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .imageUrl(imageUrl)
                .token(token)
                .build();
    }
}
