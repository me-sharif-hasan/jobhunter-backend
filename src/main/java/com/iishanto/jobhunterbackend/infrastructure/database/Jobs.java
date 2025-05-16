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
    public static Jobs fromSimpleJobModel(SimpleJobModel jobModel,Site site){
        return fromSimpleJobModel(jobModel,site,false);
    }

    public static Jobs fromSimpleJobModel(SimpleJobModel jobModel, Site site, boolean ignoreNulls) {
        Jobs job = new Jobs();

        if (!ignoreNulls || jobModel.getJobId() != null) job.setJobId(jobModel.getJobId());
        if (!ignoreNulls || jobModel.getTitle() != null) job.setTitle(jobModel.getTitle());
        if (!ignoreNulls || jobModel.getJobUrl() != null) job.setJobUrl(jobModel.getJobUrl());
        if (!ignoreNulls || jobModel.getLocation() != null) job.setLocation(jobModel.getLocation());
        if (!ignoreNulls || jobModel.getSalary() != null) job.setSalary(jobModel.getSalary());
        if (!ignoreNulls || jobModel.getJobType() != null) job.setJobType(jobModel.getJobType());
        if (!ignoreNulls || jobModel.getJobCategory() != null) job.setJobCategory(jobModel.getJobCategory());
        if (!ignoreNulls || jobModel.getJobDescription() != null) job.setJobDescription(jobModel.getJobDescription());
        if (!ignoreNulls || jobModel.getJobPostedDate() != null) job.setJobPostedDate(jobModel.getJobPostedDate());
        if (!ignoreNulls || jobModel.getJobLastDate() != null) job.setJobLastDate(jobModel.getJobLastDate());
        if (!ignoreNulls || jobModel.getJobApplyLink() != null) job.setJobApplyLink(jobModel.getJobApplyLink());
        if (!ignoreNulls || jobModel.getJobApplyEmail() != null) job.setJobApplyEmail(jobModel.getJobApplyEmail());
        if (!ignoreNulls || jobModel.getJobParsedAt() != null) job.setJobParsedAt(jobModel.getJobParsedAt());
        if (!ignoreNulls || jobModel.getJobUpdatedAt() != null) job.setJobUpdatedAt(jobModel.getJobUpdatedAt());
        if (!ignoreNulls || jobModel.getExperienceNeeded() != null) job.setExperienceNeeded(jobModel.getExperienceNeeded());
        if (!ignoreNulls || jobModel.getSkillsNeeded() != null) job.setSkillsNeeded(jobModel.getSkillsNeeded());

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
