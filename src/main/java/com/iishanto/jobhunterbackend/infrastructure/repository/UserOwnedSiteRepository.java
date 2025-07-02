package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.UserOwnedSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOwnedSiteRepository extends JpaRepository<UserOwnedSite, Long> {
    List<UserOwnedSite> findAllByUserId(Long userId);
}
