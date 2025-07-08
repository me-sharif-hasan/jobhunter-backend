package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;

import java.util.List;

public interface JobIndexUseCase {
    void refreshJobIndexWithStrategy(Long siteId);
    Long saveJobIndexStrategy(Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> pipeline);
}
