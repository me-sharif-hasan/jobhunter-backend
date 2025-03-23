package com.iishanto.jobhunterbackend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FcmTokenSaveDto {
    @NotBlank
    private String fcmToken;
}
