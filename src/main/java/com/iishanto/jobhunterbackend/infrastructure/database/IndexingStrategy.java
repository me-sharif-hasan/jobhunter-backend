package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.values.IndexingStrategyNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private IndexingStrategyNames indexingStrategy = IndexingStrategyNames.AI;
    @Lob
    private String strategyPipeline;
}
