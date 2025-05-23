package com.iishanto.jobhunterbackend.infrastructure.projection;


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

    Long getIsApplied();

    Long getSiteId();

    String getHomepage();

    String getName();

    String getIconUrl();
}
