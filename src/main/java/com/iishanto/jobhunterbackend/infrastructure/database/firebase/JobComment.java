package com.iishanto.jobhunterbackend.infrastructure.database.firebase;

import com.github.fabiomaffioletti.firebase.document.FirebaseDocument;
import com.github.fabiomaffioletti.firebase.document.FirebaseId;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@FirebaseDocument("/comment/{jobId}")
public class JobComment {
    @FirebaseId
    private String uuid;
    private String jobId;
    private String comment;
    private Timestamp createAt;
    private Timestamp updateAt;
    private Long userId;
    private String parentUuid;
    private Boolean isDeleted=false;

    //denormalized username and picture
    private String userName;
    private String userPhotoUrl;

    public SimpleJobCommentModel toSimpleJobCommentModel() {
        SimpleJobCommentModel simpleJobCommentModel = new SimpleJobCommentModel();
        simpleJobCommentModel.setJobId(jobId);
        simpleJobCommentModel.setComment(comment);
        simpleJobCommentModel.setUserId(userId);
        simpleJobCommentModel.setCreateTime(createAt);
        simpleJobCommentModel.setUpdateTime(updateAt);
        simpleJobCommentModel.setUuid(uuid);
        simpleJobCommentModel.setParentUuid(parentUuid);

        SimpleUserModel simpleUserModel = new SimpleUserModel();
        simpleUserModel.setId(userId);
        simpleUserModel.setName(userName);
        simpleUserModel.setImageUrl(userPhotoUrl);
        return simpleJobCommentModel;
    }

    public static JobComment fromSimpleJobCommentModel(SimpleJobCommentModel simpleJobCommentModel) {
        JobComment jobComment = new JobComment();
        jobComment.setUuid(simpleJobCommentModel.getJobId());
        jobComment.setComment(simpleJobCommentModel.getComment());
        jobComment.setUserId(simpleJobCommentModel.getUserId());
        jobComment.setJobId(simpleJobCommentModel.getJobId());
        jobComment.setCreateAt(simpleJobCommentModel.getCreateTime());
        jobComment.setUpdateAt(simpleJobCommentModel.getUpdateTime());
        jobComment.setParentUuid(simpleJobCommentModel.getParentUuid());
        SimpleUserModel simpleUserModel = simpleJobCommentModel.getUser();
        jobComment.setUserName(simpleUserModel.getName());
        jobComment.setUserPhotoUrl(simpleUserModel.getImageUrl());
        return jobComment;
    }
}
