package com.iishanto.jobhunterbackend.infrastructure.ports.firebase;

import com.iishanto.jobhunterbackend.domain.adapter.NotificationAdapter;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationHandlerPortTest {
    @Autowired
    JobsRepository jobsRepository;
    @Autowired
    NotificationAdapter notificationAdapter;
    @Test
    void sendJobNotification() {
        List<Jobs> jobs=jobsRepository.findAll();
        notificationAdapter.sendJobNotification(jobs.stream().map(Jobs::getJobId).toList());
    }
}