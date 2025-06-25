package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.UserOwnedSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOwnedSiteRepository extends JpaRepository<UserOwnedSite, Long> {
}
