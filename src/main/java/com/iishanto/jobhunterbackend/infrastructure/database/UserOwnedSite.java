package com.iishanto.jobhunterbackend.infrastructure.database;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Table(name = "user_owned_sites")
@Entity
public class UserOwnedSite {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "site_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Site site;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private int refreshCount;
}
