package com.iishanto.jobhunterbackend.web.dto.response.user;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import lombok.Data;

@Data
public class SafeUserResponseDto {
    private String name;
    private String email;
    private String id;
    private String photoUrl;

    public static SafeUserResponseDto from(SimpleUserModel user){
        SafeUserResponseDto safeUserResponseDto=new SafeUserResponseDto();
        safeUserResponseDto.setName(user.getName());
        safeUserResponseDto.setEmail(user.getEmail());
        safeUserResponseDto.setId(user.getId().toString());
        safeUserResponseDto.setPhotoUrl(user.getImageUrl());
        return safeUserResponseDto;
    }
}
