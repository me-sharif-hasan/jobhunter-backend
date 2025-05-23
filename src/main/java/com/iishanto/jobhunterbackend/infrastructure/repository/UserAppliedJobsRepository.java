package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.UserAppliedJobs;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAppliedJobsRepository extends JpaRepository<UserAppliedJobs, Long> {
    @Transactional
    void deleteByJob_JobIdAndUser_Id(String jobId, Long userId);

    List<UserAppliedJobs> findAllByUser_IdAndJob_TitleContainingOrJob_JobIdContainingOrJob_LocationContainingOrJob_JobDescriptionContainingOrJob_Site_NameContainingOrJob_Site_HomepageContaining(
            Long userId,
            String jobTitle,
            String jobJobId,
            String jobDescription,
            String siteName,
            String homepage,
            String jobLocation,
            Pageable pageable
    );
}
