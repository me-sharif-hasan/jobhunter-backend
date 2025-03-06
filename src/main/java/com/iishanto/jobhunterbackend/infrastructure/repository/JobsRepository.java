package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobsRepository extends JpaRepository<Jobs, String>{
    List<Jobs> findAllBySiteIn(List<Site> sites);
    @Query(value = """
    SELECT * FROM (
        SELECT j.*, ROW_NUMBER() OVER (PARTITION BY j.site_id ORDER BY j.job_updated_at DESC) AS row_num
        FROM jobs j
        WHERE j.site_id IN (:siteIds)
    ) AS subquery
    WHERE subquery.row_num <= :limit ORDER BY subquery.job_parsed_at DESC
""", nativeQuery = true)
    List<Jobs> findTopNBySiteIn(@Param("siteIds") List<Long> siteIds,@Param("limit") int limit);

    @Override
    boolean existsById(@NotNull String id);
}
