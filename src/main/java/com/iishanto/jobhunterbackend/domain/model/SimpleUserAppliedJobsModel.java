package com.iishanto.jobhunterbackend.domain.model;

import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
public class SimpleUserAppliedJobsModel {
    SimpleJobModel job;
    SimpleUserModel user;
    Long id;
    Timestamp appliedAt;
    Timestamp updatedAt;
    Timestamp createdAt;
    JobApplicationStatus applicationStatus;
}