package com.iishanto.jobhunterbackend.domain.model;

import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class SimpleJobModel {
    public String jobId;
    public String title;
    public String company;
    public String companyWebsite;
    public String jobUrl;
    public String location;
    public String salary;
    public String jobType;
    public String jobCategory;
    public String jobDescription;
    public String jobPostedDate;
    public String jobLastDate;
    public String jobApplyLink;
    public String jobApplyEmail;
    public Timestamp jobParsedAt;
    public Timestamp jobUpdatedAt;
    public String skillsNeeded;
    public String experienceNeeded;
    public boolean isDuplicate=false;
    public boolean isApproved=false;
    public Timestamp appliedAt;
    public boolean isApplied=false;
    public JobApplicationStatus applicationStatus = JobApplicationStatus.UNKNOWN;


    private SimpleSiteModel site;
    String companyIconUrl;

    public SimpleJobModel(
            String jobId,
            String title,
            String company,
            String companyWebsite,
            String companyIconUrl,
            String jobUrl,
            String location,
            String salary,
            String jobType,
            String jobCategory,
            String jobDescription,
            String jobPostedDate,
            String jobLastDate,
            String jobApplyLink,
            String jobApplyEmail,
            Timestamp jobParsedAt,
            Timestamp jobUpdatedAt,
            String skillsNeeded,
            String experienceNeeded
    ){
        this.jobId=jobId;
        this.title=title;
        this.company=company;
        this.companyWebsite=companyWebsite;
        this.companyIconUrl=companyIconUrl;
        this.jobUrl=jobUrl;
        this.location=location;
        this.salary=salary;
        this.jobType=jobType;
        this.jobCategory=jobCategory;
        this.jobDescription=jobDescription;
        this.jobPostedDate=jobPostedDate;
        this.jobLastDate=jobLastDate;
        this.jobApplyLink=jobApplyLink;
        this.jobApplyEmail=jobApplyEmail;
        this.jobParsedAt=jobParsedAt;
        this.jobUpdatedAt=jobUpdatedAt;
        this.skillsNeeded=skillsNeeded;
        this.experienceNeeded=experienceNeeded;
    }

}