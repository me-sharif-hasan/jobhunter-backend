package com.iishanto.jobhunterbackend.domain.adapter.admin;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;

import java.util.List;

public interface AdminSiteValidationDataAdapter {
    void runScript(String url, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow);
}
