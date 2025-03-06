package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;

public interface GetSubscribedJobsUseCase {
    List<SimpleJobModel> getSubscribedJobs();
}
