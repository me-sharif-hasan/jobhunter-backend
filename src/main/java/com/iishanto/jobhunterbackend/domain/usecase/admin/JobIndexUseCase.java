package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;

import java.util.List;

public interface JobIndexUseCase {
    void refreshJobIndexWithStrategy(Long siteId);
    Long saveJobIndexingProcessFlow(IndexingStrategyNames type, Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> pipeline);
    Long saveJobIdScript(IndexingStrategyNames type, Long siteId, String idString);
    List<SimpleJobModel> validateStrategyAndGetJobs(Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow);

    Long createAiIndexingStrategy(Long siteId);
}
