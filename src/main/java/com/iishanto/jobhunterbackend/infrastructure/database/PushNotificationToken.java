package com.iishanto.jobhunterbackend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class PushNotificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String token;
    private String providerName;
    private boolean isActive=true;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;
}
