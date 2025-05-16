package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Data
@Entity
public class Jobs {
    @Id
    private String jobId;
    private String title;
    @Nullable
    @Lob
    private String jobUrl;
    @Nullable
    private String location;
    @Nullable
    private String salary;
    @Nullable
    private String jobType;
    @Nullable
    private String jobCategory;
    @Nullable
    @Lob
    private String jobDescription;
    @Nullable
    @Lob
    private String skillsNeeded;
    @Nullable
    @Lob
    private String experienceNeeded;
    @Nullable
    private String jobPostedDate;
    @Nullable
    private String jobLastDate;
    @Nullable
    @Lob
    private String jobApplyLink;
    @Nullable
    private String jobApplyEmail;
    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp jobParsedAt;
    @Column(nullable = false)
    @LastModifiedDate
    private Timestamp jobUpdatedAt;
    @Column(
            nullable = false
    )
    private boolean isDescriptionIndexed=false;
    private boolean isPrivateJob=false;

    private boolean isDuplicate=false;
    private boolean isApproved=false;
    private Timestamp lastSeenAt;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    public static Jobs fromSimpleJobModel(SimpleJobModel jobModel, Site site) {
        Jobs job=new Jobs();
        job.setJobId(jobModel.getJobId());
        job.setTitle(jobModel.getTitle());
        job.setJobUrl(jobModel.getJobUrl());
        job.setLocation(jobModel.getLocation());
        job.setSalary(jobModel.getSalary());
        job.setJobType(jobModel.getJobType());
        job.setJobCategory(jobModel.getJobCategory());
        job.setJobDescription(jobModel.getJobDescription());
        job.setJobPostedDate(jobModel.getJobPostedDate());
        job.setJobLastDate(jobModel.getJobLastDate());
        job.setJobApplyLink(jobModel.getJobApplyLink());
        job.setJobApplyEmail(jobModel.getJobApplyEmail());
        job.setJobParsedAt(jobModel.getJobParsedAt());
        job.setJobUpdatedAt(jobModel.getJobUpdatedAt());
        job.setExperienceNeeded(jobModel.getExperienceNeeded());
        job.setSkillsNeeded(jobModel.getSkillsNeeded());
        job.setSite(site);
        return job;
    }


    public SimpleJobModel toSimpleJobModel(){
        SimpleJobModel simpleJobModel= new SimpleJobModel(
                jobId,
                title,
                site.getName(),
                site.getHomepage(),
                site.getIconUrl(),
                jobUrl,
                location,
                salary,
                jobType,
                jobCategory,
                jobDescription,
                jobPostedDate,
                jobLastDate,
                jobApplyLink,
                jobApplyEmail,
                jobParsedAt,
                jobUpdatedAt,
                skillsNeeded,
                experienceNeeded
        );
        simpleJobModel.setDuplicate(isDuplicate);
        simpleJobModel.setApproved(isApproved);
        return simpleJobModel;
    }
}
