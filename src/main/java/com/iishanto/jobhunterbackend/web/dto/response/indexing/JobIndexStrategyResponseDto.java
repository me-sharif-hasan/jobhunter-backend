package com.iishanto.jobhunterbackend.web.dto.response.indexing;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobIndexStrategyResponseDto {
    private IndexingStrategyNames type;
    private List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow;
}
