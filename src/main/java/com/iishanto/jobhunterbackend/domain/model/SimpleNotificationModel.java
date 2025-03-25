package com.iishanto.jobhunterbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
public class SimpleNotificationModel {
    private Long id;
    private String title;
    private String body;
    private String iconUrl;
    private String resourceAction;
    private String resourceId;
    private Timestamp createdAt;
}
