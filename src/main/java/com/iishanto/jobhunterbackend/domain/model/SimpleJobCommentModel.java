package com.iishanto.jobhunterbackend.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
