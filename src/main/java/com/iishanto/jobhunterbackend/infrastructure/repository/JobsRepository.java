package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.projection.PersonalJobProjection;
import jakarta.persistence.LockModeType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface JobsRepository extends JpaRepository<Jobs, String> {
    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden,
            if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
        FROM jobs
        JOIN site ON site.id = jobs.site_id
        LEFT JOIN user_applied_jobs
            ON user_applied_jobs.job_id = jobs.job_id
            AND user_applied_jobs.user_id = :userId
        WHERE
            jobs.is_duplicate = false
            AND (jobs.is_present_on_site is null OR jobs.is_present_on_site != false)
            AND jobs.site_id IN :siteIds
            AND site.is_published = TRUE
            AND (
                lower(jobs.title) LIKE concat('%', lower(:keyword), '%')
                OR lower(jobs.job_description) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
        ORDER BY sorted_order DESC
    """, nativeQuery = true)
    Page<PersonalJobProjection> findJobs(@Param("siteIds") List<Long> siteIds, @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden,
            if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
        FROM jobs
        JOIN site ON site.id = jobs.site_id
        JOIN user_applied_jobs
            ON (user_applied_jobs.job_id = jobs.job_id AND user_applied_jobs.user_id = :userId)
        WHERE
            jobs.is_duplicate = false
            AND jobs.site_id IN :siteIds
            AND (
                lower(jobs.title) LIKE concat('%', lower(:keyword), '%')
                OR lower(jobs.job_description) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
        ORDER BY sorted_order DESC
    """, nativeQuery = true)
    Page<PersonalJobProjection> findAppliedJobs(@Param("siteIds") List<Long> siteIds, @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);


    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden,
            user_applied_jobs.application_status as application_status,
            if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
        FROM jobs
        JOIN site ON site.id = jobs.site_id
        JOIN user_applied_jobs
            ON user_applied_jobs.job_id = jobs.job_id
            AND user_applied_jobs.user_id = :userId
            AND user_applied_jobs.is_applied = true
        WHERE
            jobs.is_duplicate = false
            AND (
                lower(jobs.title) LIKE concat('%', lower(:keyword), '%')
                OR lower(jobs.job_description) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
        ORDER BY sorted_order DESC
    """, nativeQuery = true)
    Page<PersonalJobProjection> findAppliedJobs(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Override
    boolean existsById(@NotNull String id);

    boolean existsByJobUrl(String jobUrl);

    List<Jobs> findAllByJobIdIn(List<String> jobIds);

    List<Jobs> findAllByJobDescriptionContainingOrTitleContainingOrLocationContainingOrderByJobParsedAtDesc(String description, String title, String location, Pageable pageable);

    Optional<Jobs> findByJobUrl(String jobUrl);

    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden,
            if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
        FROM jobs
        LEFT JOIN site ON site.id = jobs.site_id
        LEFT JOIN user_applied_jobs
            ON user_applied_jobs.job_id = jobs.job_id
            AND user_applied_jobs.user_id = :userId
        WHERE
            (jobs.site_id = :siteId OR :siteId IS NULL OR :siteId <0)
            AND
                (jobs.is_present_on_site is null OR jobs.is_present_on_site != false)
            AND
            jobs.is_duplicate = false
            AND site.is_published = TRUE
            AND (
                lower(jobs.title) LIKE concat('%', lower(:keyword), '%')
                OR lower(jobs.job_description) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
        ORDER BY sorted_order DESC
    """, nativeQuery = true)
    List<PersonalJobProjection> findAllJobs(String keyword,Long userId,Long siteId, Pageable pageable);

    List<Jobs> findJobsByJobIdNotInAndSiteId(Set<String> jobId, Long siteId);
}
