package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site,Long> {
    Optional<Site> findByJobListPageUrl(String jobListPageUrl);
    Optional<Site> findByHomepage(String homePage);
    List<Site> findAllByOrderByCreatedAtDesc();
    List<Site> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<Site> findAllByLastCrawledAtBefore(Timestamp timestamp);

    //search query given search on name
    List<Site> findAllByNameContainingOrDescriptionContainingOrderByCreatedAtDesc(String query,String description,Pageable pageable);
}
