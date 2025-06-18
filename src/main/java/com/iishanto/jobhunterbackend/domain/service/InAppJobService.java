package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.JobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.NotificationAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleNotificationModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.GetPersonalInAppNotificationsUseCase;
import com.iishanto.jobhunterbackend.domain.usecase.JobCommentUseCase;
import lombok.AllArgsConstructor;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InAppJobService implements GetPersonalInAppNotificationsUseCase, JobCommentUseCase {
    private NotificationAdapter notificationAdapter;
    JobDataAdapter jobDataAdapter;
    UserDataAdapter userDataAdapter;
    @Override
    public List<SimpleNotificationModel> getNotifications(int page, int limit) {
        return notificationAdapter.getInAppNotification(page,limit);
    }

    @Override
    public SimpleJobCommentModel addJobComment(SimpleJobCommentModel model) {
        if(StringUtil.isBlank(model.getComment())){
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        if(StringUtil.isBlank(model.getJobId())){
            throw new IllegalArgumentException("JobId cannot be empty");
        }
        SimpleUserModel user = userDataAdapter.getLoggedInUser();
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        model.setUserId(user.getId());
        model.setUser(user);
        SimpleJobCommentModel commentModel = jobDataAdapter.postComment(model);
        commentModel.setUser(user);
        return commentModel;
    }

    @Override
    public List<SimpleJobCommentModel> getAllJobComments(String jobId, int limit, Long startAt) {
        if(StringUtil.isBlank(jobId)){
            throw new IllegalArgumentException("JobId cannot be empty");
        }
        if(limit <= 0){
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        return jobDataAdapter.getJobComments(jobId,limit,startAt).stream().peek(jobCommentModel -> {
            SimpleUserModel user = userDataAdapter.getUserById(jobCommentModel.getUserId());
            jobCommentModel.setUser(user);
        }).collect(Collectors.toList());
    }
}
