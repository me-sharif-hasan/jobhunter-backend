package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.iishanto.jobhunterbackend.infrastructure.database.PushNotificationToken;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushNotificationTokenRepository extends JpaRepository<PushNotificationToken, Long> {
    List <PushNotificationToken> findAllByUserIn(List<User> user);
}
