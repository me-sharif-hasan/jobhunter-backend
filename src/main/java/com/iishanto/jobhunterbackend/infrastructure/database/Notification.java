package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleNotificationModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String body;
    private String iconUrl;
    @ManyToOne
    private User user;
    private String resourceAction;
    private String resourceId;

    @CreationTimestamp
    private Timestamp createdAt;

    public SimpleNotificationModel toSimpleNotificationModel(){
        SimpleNotificationModel model=SimpleNotificationModel.builder()
                .id(this.id)
                .title(this.title)
                .body(this.body)
                .iconUrl(this.iconUrl)
                .resourceAction(this.resourceAction)
                .resourceId(this.resourceId)
                .createdAt(this.createdAt)
                .build();
        return model;
    }
}
