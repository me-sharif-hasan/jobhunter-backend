package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;
import com.iishanto.jobhunterbackend.infrastructure.projection.PersonalJobProjection;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface JobsRepository extends JpaRepository<Opportunity, String> {
    @Query(value = """
                SELECT
                    opportunity.*,
                    site.*,
                    user_applied_jobs.is_applied as is_applied,
                    user_applied_jobs.applied_at as applied_at,
                    user_applied_jobs.is_favourite as is_favourite,
                    user_applied_jobs.is_hidden as is_hidden,
                    if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
                FROM opportunity
                JOIN site ON site.id = opportunity.site_id
                LEFT JOIN user_applied_jobs
                    ON user_applied_jobs.job_id = opportunity.job_id
                    AND user_applied_jobs.user_id = :userId
                WHERE
                    opportunity.is_duplicate = false
                    AND (opportunity.is_present_on_site is null OR opportunity.is_present_on_site != false)
                    AND opportunity.site_id IN :siteIds
                    AND (
                        lower(opportunity.title) LIKE concat('%', lower(:keyword), '%')
                        OR lower(opportunity.job_description) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
                ORDER BY sorted_order DESC
            """, nativeQuery = true)
    Page<PersonalJobProjection> findJobs(@Param("siteIds") List<Long> siteIds, @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
                SELECT
                    opportunity.*,
                    site.*,
                    user_applied_jobs.is_applied as is_applied,
                    user_applied_jobs.applied_at as applied_at,
                    user_applied_jobs.is_favourite as is_favourite,
                    user_applied_jobs.is_hidden as is_hidden,
                    if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
                FROM opportunity
                JOIN site ON site.id = opportunity.site_id
                JOIN user_applied_jobs
                    ON (user_applied_jobs.job_id = opportunity.job_id AND user_applied_jobs.user_id = :userId)
                WHERE
                    opportunity.is_duplicate = false
                    AND opportunity.site_id IN :siteIds
                    AND (
                        lower(opportunity.title) LIKE concat('%', lower(:keyword), '%')
                        OR lower(opportunity.job_description) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
                ORDER BY sorted_order DESC
            """, nativeQuery = true)
    Page<PersonalJobProjection> findAppliedJobs(@Param("siteIds") List<Long> siteIds, @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);


    @Query(value = """
                SELECT
                    opportunity.*,
                    site.*,
                    user_applied_jobs.is_applied as is_applied,
                    user_applied_jobs.applied_at as applied_at,
                    user_applied_jobs.is_favourite as is_favourite,
                    user_applied_jobs.is_hidden as is_hidden,
                    user_applied_jobs.application_status as application_status,
                    if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
                FROM opportunity
                JOIN site ON site.id = opportunity.site_id
                JOIN user_applied_jobs
                    ON user_applied_jobs.job_id = opportunity.job_id
                    AND user_applied_jobs.user_id = :userId
                    AND user_applied_jobs.is_applied = true
                WHERE
                    opportunity.is_duplicate = false
                    AND (
                        lower(opportunity.title) LIKE concat('%', lower(:keyword), '%')
                        OR lower(opportunity.job_description) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
                ORDER BY sorted_order DESC
            """, nativeQuery = true)
    Page<PersonalJobProjection> findAppliedJobs(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Override
    boolean existsById(@NotNull String id);

    boolean existsByJobUrl(String jobUrl);

    List<Opportunity> findAllByJobIdIn(List<String> jobIds);

    List<Opportunity> findAllByJobDescriptionContainingOrTitleContainingOrLocationContainingOrderByJobParsedAtDesc(String description, String title, String location, Pageable pageable);

    Optional<Opportunity> findByJobUrl(String jobUrl);

    @Query(value = """
                SELECT
                    opportunity.*,
                    site.*,
                    user_applied_jobs.is_applied as is_applied,
                    user_applied_jobs.applied_at as applied_at,
                    user_applied_jobs.is_favourite as is_favourite,
                    user_applied_jobs.is_hidden as is_hidden,
                    if(reopen_noticed_at>job_parsed_at, reopen_noticed_at,job_parsed_at) as sorted_order
                FROM opportunity
                LEFT JOIN site ON site.id = opportunity.site_id
                LEFT JOIN user_applied_jobs
                    ON user_applied_jobs.job_id = opportunity.job_id
                    AND user_applied_jobs.user_id = :userId
                WHERE
                    (opportunity.site_id = :siteId OR :siteId IS NULL OR :siteId <0)
                    AND
                        (opportunity.is_present_on_site is null OR opportunity.is_present_on_site != false)
                    AND
                    opportunity.is_duplicate = false
                    AND site.is_published = TRUE
                    AND (
                        lower(opportunity.title) LIKE concat('%', lower(:keyword), '%')
                        OR lower(opportunity.job_description) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.name) LIKE concat('%', lower(:keyword), '%')
                        OR lower(site.homepage) LIKE concat('%', lower(:keyword), '%')            )
                ORDER BY sorted_order DESC
            """, nativeQuery = true)
    List<PersonalJobProjection> findAllJobs(String keyword, Long userId, Long siteId, Pageable pageable);

    List<Opportunity> findJobsByJobIdNotInAndSiteId(Set<String> jobId, Long siteId);


    @Query(
            value = """
                    select is_present_on_site from opportunity where job_id = :jobId
                    """, nativeQuery = true
    )
    boolean isPresentOnSite(String jobId);

    List<Opportunity> findAllByJobIdNotIn(Collection<String> jobIds);
}
