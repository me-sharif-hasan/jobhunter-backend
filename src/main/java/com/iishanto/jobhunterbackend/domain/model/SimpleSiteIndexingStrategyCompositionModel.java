package com.iishanto.jobhunterbackend.domain.model;

import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SimpleSiteIndexingStrategyCompositionModel {
    private Long id;
    private IndexingStrategyNames indexingStrategy = IndexingStrategyNames.AI;
    private List<SiteAttributeValidatorModel.JobExtractionPipeline> strategyPipeline;
}
