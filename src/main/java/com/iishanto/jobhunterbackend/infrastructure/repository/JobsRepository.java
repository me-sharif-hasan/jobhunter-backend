package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.projection.PersonalJobProjection;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JobsRepository extends JpaRepository<Jobs, String> {
    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden
        FROM jobs
        JOIN site ON site.id = jobs.site_id
        LEFT JOIN user_applied_jobs
            ON user_applied_jobs.job_id = jobs.job_id
            AND user_applied_jobs.user_id = :userId
        WHERE
            jobs.is_duplicate = false
            AND jobs.site_id IN :siteIds
            AND (
                jobs.title LIKE %:keyword%
                OR jobs.job_description LIKE %:keyword%
                OR site.name LIKE %:keyword%
            )
        ORDER BY jobs.job_parsed_at DESC
    """, nativeQuery = true)
    Page<PersonalJobProjection> findJobs(@Param("siteIds") List<Long> siteIds, @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden
        FROM jobs
        JOIN site ON site.id = jobs.site_id
        JOIN user_applied_jobs
            ON (user_applied_jobs.job_id = jobs.job_id AND user_applied_jobs.user_id = :userId)
        WHERE
            jobs.is_duplicate = false
            AND jobs.site_id IN :siteIds
            AND (
                jobs.title LIKE %:keyword%
                OR jobs.job_description LIKE %:keyword%
                OR site.name LIKE %:keyword%
            )
        ORDER BY jobs.job_parsed_at DESC
    """, nativeQuery = true)
    Page<PersonalJobProjection> findAppliedJobs(@Param("siteIds") List<Long> siteIds, @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);


    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden
        FROM jobs
        JOIN site ON site.id = jobs.site_id
        JOIN user_applied_jobs
            ON user_applied_jobs.job_id = jobs.job_id
            AND user_applied_jobs.user_id = :userId
            AND user_applied_jobs.is_applied = true
        WHERE
            jobs.is_duplicate = false
            AND (
                jobs.title LIKE %:keyword%
                OR jobs.job_description LIKE %:keyword%
                OR site.name LIKE %:keyword%
            )
        ORDER BY jobs.job_parsed_at DESC
    """, nativeQuery = true)
    Page<PersonalJobProjection> findAppliedJobs(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Override
    boolean existsById(@NotNull String id);

    boolean existsByJobUrl(String jobUrl);

    List<Jobs> findAllByJobIdIn(List<String> jobIds);

    List<Jobs> findAllByJobDescriptionContainingOrTitleContainingOrLocationContainingOrderByJobParsedAtDesc(String description, String title, String location, Pageable pageable);

    Optional<Jobs> findByJobUrl(String jobUrl);

//    @Modifying
//    @Transactional
//    @Query(value = """
//    INSERT INTO user_applied_jobs(user_id, job_id)
//    SELECT :userId, :jobId FROM DUAL
//    WHERE NOT EXISTS (
//        SELECT 1 FROM user_applied_jobs
//        WHERE user_id = :userId AND job_id = :jobId
//    )
//    """, nativeQuery = true)
//    Integer applyIfNotApplied(@Param("userId") Long userId, @Param("jobId") String jobId);
//
//    @Modifying
//    @Transactional
//    @Query(value = """
//        DELETE FROM user_applied_jobs
//        WHERE user_id = :userId AND job_id = :jobId
//        """, nativeQuery = true)
//    Integer unapplyIfApplied(Long userId, String jobId);

    @Query(value = """
        SELECT
            jobs.*,
            site.*,
            user_applied_jobs.is_applied as is_applied,
            user_applied_jobs.applied_at as applied_at,
            user_applied_jobs.is_favourite as is_favourite,
            user_applied_jobs.is_hidden as is_hidden
        FROM jobs
        LEFT JOIN site ON site.id = jobs.site_id
        LEFT JOIN user_applied_jobs
            ON user_applied_jobs.job_id = jobs.job_id
            AND user_applied_jobs.user_id = :userId
        WHERE
            (jobs.site_id = :siteId OR :siteId IS NULL OR :siteId <0)
            AND
            jobs.is_duplicate = false
            AND (
                jobs.title LIKE %:keyword%
                OR jobs.job_description LIKE %:keyword%
                OR site.name LIKE %:keyword%
            )
        ORDER BY jobs.job_parsed_at DESC
    """, nativeQuery = true)
    List<PersonalJobProjection> findAllJobs(String keyword,Long userId,Long siteId, Pageable pageable);
}
