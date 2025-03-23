package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.database.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{
    Optional<Subscription> findFirstByUserIdAndSiteId(Long userId, Long siteId);
    List<Subscription> findAllByUserId(Long userId);
    List<Subscription> findAllBySite(Site site);

    List<Subscription> findAllByUserIdAndSiteIdIn(Long userId, List<Long> siteIds);
}
