package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SiteRepository extends JpaRepository<Site,Long> {
    Optional<Site> findByJobListPageUrl(String jobListPageUrl);
    Optional<Site> findByHomepage(String homePage);
    List<Site> findAllByLastCrawledAtBefore(Timestamp timestamp);

    //search query given search on name
    List<Site> findAllByNameContainingAndIsPublishedTrueOrDescriptionContainingAndIsPublishedTrueOrderByCreatedAtDesc(String query, String description, Pageable pageable);

    List<Site> findAllByIdInOrderByLastCrawledAtDesc(Set<Long> siteIds);

    @Modifying
    @Transactional
    @Query("UPDATE Site s SET s.lastCrawledAt = ?2 WHERE s.id = ?1")
    void updateLastIndexedDate(Long id, Timestamp timestamp);
}
