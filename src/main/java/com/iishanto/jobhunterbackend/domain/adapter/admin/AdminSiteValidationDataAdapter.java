package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.service.admin.JobIndexingService;

import java.util.List;

public interface AdminSiteValidationDataAdapter {
    void executeProcessFlow(String url, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow, JobIndexingService.OnJobAvailableCallback onJobAvailableCallback);
}
