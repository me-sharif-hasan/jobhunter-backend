package com.iishanto.jobhunterbackend.web.dto.response.comment;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.web.dto.response.user.SafeUserResponseDto;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SafeJobCommentDto {
    SafeUserResponseDto user;

    String uuid;
    String comment;
    String jobId;
    Long userId;
    String parentUuid;

    Timestamp createTime;
    Timestamp updateTime;

    public static SafeJobCommentDto from(SimpleJobCommentModel jobCommentModel){
        SafeJobCommentDto dto = new SafeJobCommentDto();
        dto.setUuid(jobCommentModel.getUuid());
        dto.setComment(jobCommentModel.getComment());
        dto.setJobId(jobCommentModel.getJobId());
        dto.setUserId(jobCommentModel.getUserId());
        dto.setParentUuid(jobCommentModel.getParentUuid());
        dto.setCreateTime(jobCommentModel.getCreateTime());
        dto.setUpdateTime(jobCommentModel.getUpdateTime());
        dto.setUser(SafeUserResponseDto.from(jobCommentModel.getUser()));
        return dto;
    }
}
