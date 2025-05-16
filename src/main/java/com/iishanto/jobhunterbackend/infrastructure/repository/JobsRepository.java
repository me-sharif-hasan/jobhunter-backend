package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobsRepository extends JpaRepository<Jobs, String> {
    @Query(value = "SELECT * FROM jobs,site WHERE site.is_duplicate = false AND (site.id = site_id AND site_id IN :siteIds AND (title LIKE %:keyword% OR job_description LIKE %:keyword% OR site.name LIKE %:keyword%)) ORDER BY job_parsed_at DESC", nativeQuery = true)
    Page<Jobs> findJobs(@Param("siteIds") List<Long> siteIds, @Param("keyword") String keyword, Pageable pageable);

    @Override
    boolean existsById(@NotNull String id);

    boolean existsByJobUrl(String jobUrl);

    List<Jobs> findAllByJobIdIn(List<String> jobIds);

    List<Jobs> findAllByJobDescriptionContainingOrTitleContainingOrLocationContainingOrderByJobParsedAtDesc(String description, String title, String location, Pageable pageable);

    Optional<Jobs> findByJobUrl(String jobUrl);
}
