package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;

public interface UserJobAccessUseCase {
    List<SimpleJobModel> getSubscribedJobs();
    List<SimpleJobModel> getSubscribedJobs(int page,int limit,String query,int siteId);

    List<SimpleJobModel> getAppliedJobs(int page, int limit, String query, int siteId);

    List<SimpleJobModel> getAllJobs(int page, int limit, String query, int siteId);
}
