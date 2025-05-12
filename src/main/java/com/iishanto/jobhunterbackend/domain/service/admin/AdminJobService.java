package com.iishanto.jobhunterbackend.domain.service.admin;

import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminJobDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.usecase.admin.GetAllJobsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminJobService implements GetAllJobsUseCase {
    AdminJobDataAdapter adminJobDataAdapter;
    @Override
    public List<SimpleJobModel> getAllJobs(int page, int limit, String query) {
        return adminJobDataAdapter.getAllJobsForAdmin(page, limit, query);
    }

    @Override
    public long getTotalJobsCount() {
        return adminJobDataAdapter.getTotalJobsCount();
    }
}
