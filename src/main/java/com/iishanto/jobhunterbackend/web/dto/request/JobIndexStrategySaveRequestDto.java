package com.iishanto.jobhunterbackend.web.dto.request;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import lombok.Data;

import java.util.List;

@Data
public class JobIndexStrategySaveRequestDto {
    private String type;
    private List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow;
}
