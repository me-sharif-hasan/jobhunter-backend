package com.iishanto.jobhunterbackend.infrastructure.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JobsRepositoryTest {
    @Autowired
    JobsRepository jobsRepository;
    @Test
    void existsByJobUrl() {
        assertTrue(jobsRepository.existsByJobUrl("https://jobs.bdjobs.com/jobdetails.asp?id=1296623&fcatId=5&ln=1"));
    }
}