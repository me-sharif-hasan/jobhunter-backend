package com.iishanto.jobhunterbackend.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SimpleJobCommentModel {
    SimpleUserModel user;

    String uuid;
    String comment;
    String jobId;
    Long userId;
    String parentUuid;

    Timestamp createTime;
    Timestamp updateTime;
}
