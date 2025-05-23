package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.JobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.JobApplyManagementUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobService implements JobApplyManagementUseCase {
    UserDataAdapter userDataAdapter;
    JobDataAdapter jobDataAdapter;
    @Override
    public void markApplied(String jobId) {
        SimpleUserModel simpleUserModel = userDataAdapter.getLoggedInUser();
        if (simpleUserModel == null) {
            throw new RuntimeException("User not found");
        }
        jobDataAdapter.markApplied(jobId, simpleUserModel.getId());
    }

    @Override
    public void unmarkApplied(String jobId) {
        SimpleUserModel simpleUserModel = userDataAdapter.getLoggedInUser();
        if (simpleUserModel == null) {
            throw new RuntimeException("User not found");
        }
        jobDataAdapter.unmarkApplied(jobId, simpleUserModel.getId());
    }
}
