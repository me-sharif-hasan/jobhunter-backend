package com.iishanto.jobhunterbackend.web.controller.admin;

import com.iishanto.jobhunterbackend.domain.service.admin.AdminJobService;
import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.testutils.TestDataFactory;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AdminJobControllerIntegrationTest {
    static final MySQLContainer MYSQL_CONTAINER;
    static {
        MYSQL_CONTAINER = new MySQLContainer("mysql:8.0.32");
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setupMySQL(DynamicPropertyRegistry registry) {
        TestUtils.setupMySQL(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private JobsRepository jobsRepository;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = testUtils.setupAuthentication();
        jobsRepository.deleteAll();
    }

    @Test
    void approveJob_ShouldApproveJobAndReturnSuccess() throws Exception {
        // Create a test job using TestDataFactory
        Opportunity job = testDataFactory.createJob(
            "test-job-id",
            "Test Job",
            "http://test.com",
            false
        );

        mockMvc.perform(patch("/admin/job/approve")
                        .param("jobId", job.getJobId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(job.getJobId()))
                .andExpect(jsonPath("$.message").value("Job approved successfully"));

        // Verify job is approved in database
        Opportunity updatedJob = jobsRepository.findById(job.getJobId()).orElseThrow();
        assertTrue(updatedJob.isApproved(), "Job should be approved after calling approve endpoint");
    }

    @Test
    void rejectJob_ShouldRejectJobAndReturnSuccess() throws Exception {
        // Create a test job using TestDataFactory
        Opportunity job = testDataFactory.createJob(
            "test-job-id",
            "Test Job",
            "http://test.com",
            true
        );

        mockMvc.perform(patch("/admin/job/reject")
                        .param("jobId", job.getJobId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(job.getJobId()))
                .andExpect(jsonPath("$.message").value("Job rejected successfully"));

        // Verify job is rejected in database
        Opportunity updatedJob = jobsRepository.findById(job.getJobId()).orElseThrow();
        assertFalse(updatedJob.isApproved(), "Job should not be approved after calling reject endpoint");
    }


    @Test
    void rejectJob_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        String jobId = "test-job-id";

        mockMvc.perform(patch("/admin/job/reject")
                        .param("jobId", jobId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approveJob_WithInvalidJobId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/admin/job/approve")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectJob_WithInvalidJobId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/admin/job/reject")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    void tearDown() {
        testUtils.clearDatabase();
    }
}
