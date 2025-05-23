package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.JobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.usecase.JobApplyManagementUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<SimpleUserAppliedJobsModel> getAppliedJobs(int page, int limit, String query) {
        if(page < 0) {
            throw new IllegalArgumentException("Page can not be less than zero");
        }
        if(limit > 50) {
            throw new IllegalArgumentException("Too high limit, max is 50");
        }
        if(query==null) query="";
        if (query.length() > 25) {
            throw new IllegalArgumentException("Too long query");
        }
        return jobDataAdapter.getAppliedJobs(
                userDataAdapter.getLoggedInUser().getId(),
                page,
                limit,
                query
        );
    }
}
