package com.iishanto.jobhunterbackend.web.dto.request;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import lombok.Data;

import java.util.List;

@Data
public class JobIndexStrategySaveRequestDto {
    private IndexingStrategyNames type;
    private String idScript;
    private List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow;
}
