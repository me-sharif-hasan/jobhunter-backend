package com.iishanto.jobhunterbackend.infrastructure.database;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Table(name = "user_owned_sites",uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"user_id", "site_id"},
                name = "unique_user_site"
        )
})
@Entity
@Data
public class UserOwnedSite {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "site_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Site site;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    private int refreshCount=0;
}
