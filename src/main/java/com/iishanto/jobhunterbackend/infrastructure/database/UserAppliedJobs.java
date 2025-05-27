package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;
import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import jakarta.persistence.*;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Table(
        name = "user_applied_jobs",
        uniqueConstraints = {
                @jakarta.persistence.UniqueConstraint(columnNames = {"user_id", "job_id"})
        }
)
@Entity
@Data
public class UserAppliedJobs {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "job_id", nullable = false)
        private Jobs job;

        @Column(nullable = false, updatable = false)
        @CreationTimestamp
        private Timestamp createdAt;

        @Column(nullable = false)
        @UpdateTimestamp
        private Timestamp updatedAt;

        private boolean isApplied=false;
        private Timestamp appliedAt;
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private JobApplicationStatus applicationStatus = JobApplicationStatus.DEFAULT;

        private boolean isFavourite=false;
        private boolean isHidden=false;

        @PrePersist
        protected void onCreate() {
                this.appliedAt = new Timestamp(System.currentTimeMillis());;
                if (this.applicationStatus == null) {
                        this.applicationStatus = JobApplicationStatus.APPLIED;
                }
        }

        public SimpleUserAppliedJobsModel toUserAppliedJobsModel() {
                return new SimpleUserAppliedJobsModel(
                        job.toSimpleJobModel(),
                        user.toUserModel(),
                        getId(),
                        getAppliedAt(),
                        getUpdatedAt(),
                        getCreatedAt(),
                        getApplicationStatus()
                );
        }
}
