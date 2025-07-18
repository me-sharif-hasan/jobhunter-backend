package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.IndexingStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndexingStrategyRepository extends JpaRepository<IndexingStrategy, Long> {
    List<IndexingStrategy> findAllBySiteIdIn(List<Long> siteIds);
    Optional<IndexingStrategy> findBySiteId(Long siteId);
}
