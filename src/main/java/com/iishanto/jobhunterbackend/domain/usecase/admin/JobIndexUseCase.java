package com.iishanto.jobhunterbackend.domain.usecase.admin;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import com.iishanto.jobhunterbackend.web.dto.response.indexing.JobIndexStrategyResponseDto;

import java.util.List;

public interface JobIndexUseCase {
    void refreshJobIndexWithStrategy(Long siteId);
    Long saveJobIndexStrategy(IndexingStrategyNames type, Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> pipeline);


    List<SimpleJobModel> validateStrategyAndGetJobs(Long siteId, List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow);

}
