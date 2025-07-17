package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.UserResume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResumeRepository extends JpaRepository<UserResume,Long> {
    UserResume findByUser_Id(Long userId);
}
