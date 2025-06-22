package com.iishanto.jobhunterbackend.infrastructure.projection;


import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;

import java.sql.Timestamp;

public interface PersonalJobProjection {

    String getJobId();
    String getTitle();
    String getJobUrl();
    String getLocation();
    String getSalary();
    String getJobType();
    String getJobCategory();
    String getJobDescription();
    String getSkillsNeeded();
    String getExperienceNeeded();
    String getJobPostedDate();
    String getJobLastDate();
    String getJobApplyLink();
    String getJobApplyEmail();
    Timestamp getJobParsedAt();
    Timestamp getJobUpdatedAt();

    Boolean getIsDescriptionIndexed();
    Boolean getIsPrivateJob();
    Boolean getIsDuplicate();
    Boolean getIsApproved();

    Boolean getIsApplied();

    Long getSiteId();

    String getHomepage();

    String getName();

    String getIconUrl();

    Timestamp getAppliedAt();
    JobApplicationStatus getApplicationStatus();
    Boolean getIsPresentOnSite();
    Boolean getIsReopened();
    Timestamp getReopenNoticedAt();
}
