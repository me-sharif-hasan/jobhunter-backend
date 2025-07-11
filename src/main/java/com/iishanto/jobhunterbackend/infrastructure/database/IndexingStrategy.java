package com.iishanto.jobhunterbackend.infrastructure.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.domain.model.SimpleSiteIndexingStrategyCompositionModel;
import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "indexing_strategy")
public class IndexingStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Enumerated(EnumType.STRING)
    private IndexingStrategyNames indexingStrategy = IndexingStrategyNames.AI;
    @Lob
    private String strategyPipeline;

    public SimpleSiteIndexingStrategyCompositionModel toDomain() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SiteAttributeValidatorModel.JobExtractionPipeline> processFlow = mapper.readValue(
                strategyPipeline, mapper.getTypeFactory().constructCollectionType(List.class, SiteAttributeValidatorModel.JobExtractionPipeline.class)
        );
        return SimpleSiteIndexingStrategyCompositionModel.builder()
                .id(id)
                .indexingStrategy(indexingStrategy)
                .strategyPipeline(processFlow)
                .build();
    }
}
