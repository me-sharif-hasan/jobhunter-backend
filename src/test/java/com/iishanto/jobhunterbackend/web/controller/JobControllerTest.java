package com.iishanto.jobhunterbackend.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JobControllerTest {

    @Autowired
    JobController jobController;
    @Test
    void refreshJobs() {
        jobController.refreshJobs();
    }
}