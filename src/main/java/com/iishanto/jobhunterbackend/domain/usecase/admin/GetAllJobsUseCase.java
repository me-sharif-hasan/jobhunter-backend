package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;

public interface GetAllJobsUseCase {
    List<SimpleJobModel> getAllJobs(int page, int limit, String query);
    long getTotalJobsCount();
}
