package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.config.security.JwtUtil;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.database.firebase.JobComment;
import com.iishanto.jobhunterbackend.infrastructure.ports.database.UserDataPort;
import com.iishanto.jobhunterbackend.infrastructure.repository.*;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class JobControllerTest {
    static final MySQLContainer MYSQL_CONTAINER;
    static {
        MYSQL_CONTAINER = new MySQLContainer("mysql:8.0.32");
        MYSQL_CONTAINER.start();
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils  testUtils;

    @DynamicPropertySource
    static void setProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        System.out.println("Setting up MySQL container properties");
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("log4j.logger.org.testcontainers", () -> "DEBUG");
        registry.add("google.gemini.apikey", () -> "TEST_GEMINI_API_KEY");
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtUtil jwtUtil;


    @Autowired
    UserDataPort userDataPort;
    @Autowired
    JobsRepository jobsRepository;
    @Autowired
    SiteRepository siteRepository;

    @BeforeEach
    void setUpAuthentication(){
        testUtils.setupAuthentication();
        setupJobs();
    }

    String generateToken() {
        return jwtUtil.generateToken("test@gmail.com", "ROLE_USER");
    }

    void setupJobs(){
        Site site = new Site();
        site.setName("Test Site");
        site.setJobListPageUrl("https://example.com");
        site.setDescription("This is a test site for job postings.");
        site = siteRepository.save(site);

        if(jobsRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                Opportunity job= Opportunity.fromSimpleJobModel(
                        SimpleJobModel.builder()
                                .jobId("test-job-id-" + i)
                                .title("Test Job " + i)
                                .jobType("Full Time")
                                .location("Test Location " + i)
                                .jobDescription("This is a test job description for job " + i)
                                .jobUrl("https://example.com/job/" + i)
                                .jobPostedDate("2023-10-01")
                                .jobLastDate("2023-10-31")
                                .jobUpdatedAt(Timestamp.from(Instant.now()))
                                .jobParsedAt(Timestamp.from(Instant.now()))
                                .site(site.toDomain())
                                .jobApplyLink("https://example.com/apply/" + i)
                                .build(),
                        site
                );
                job.setIsPresentOnSite(true);
                jobsRepository.save(job);
            }
        }

        Site subscribedSite = new Site();
        subscribedSite.setName("Subscribed Site");
        subscribedSite.setJobListPageUrl("https://subscribed.example.com");
        subscribedSite.setHomepage("https://example-subscribed.com");
        subscribedSite.setDescription("This is a subscribed site for job postings.");
        subscribedSite = siteRepository.save(subscribedSite);
        for (int i = 0; i < 5; i++) {
            Opportunity job = Opportunity.fromSimpleJobModel(
                    SimpleJobModel.builder()
                            .jobId("subscribed-job-id-" + i)
                            .title("Subscribed Job " + i)
                            .jobType("Part Time")
                            .location("Subscribed Location " + i)
                            .jobDescription("This is a subscribed job description for job " + i)
                            .jobUrl("https://subscribed.example.com/job/" + i)
                            .jobPostedDate("2023-10-01")
                            .jobLastDate("2023-10-31")
                            .jobUpdatedAt(Timestamp.from(Instant.now()))
                            .jobParsedAt(Timestamp.from(Instant.now()))
                            .site(subscribedSite.toDomain())
                            .jobApplyLink("https://subscribed.example.com/apply/" + i)
                            .build(),
                    subscribedSite
            );
            job.setIsPresentOnSite(true);
            jobsRepository.save(job);
        }
    }

    private void setupSubscriptions() throws Exception{
        Site site = siteRepository.findByHomepage("https://example-subscribed.com").orElseThrow();
        mockMvc.perform(
                post("/api/subscription")
                        .content(
                                """
                                {
                                    "site_id": %d
                                }
                                """.formatted(site.getId())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        );
    }

    @Test
    void getJobs_When_Fetching_All_Jobs() throws Exception {
        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .param("variant","all")
                        .param("query","Test Job")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(10)
        ).andExpect(
                jsonPath("$.data[*].company").value(everyItem(is("Test Site")))
        );
    }

    @Test
    void getJobs_When_Fetching_Subscribed_Jobs_With_No_Subscription() throws Exception {
        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .param("variant","subscribed")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(0)
        );
    }

    @Test
    void getJobs_When_Fetching_Subscribed_Jobs_With_Subscription() throws Exception {
        setupSubscriptions();
        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .param("variant","subscribed")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(5)
        ).andExpect(
                jsonPath("$.data[*].company").value(everyItem(is("Subscribed Site")))
        );
    }

    @Test
    void getJobs_When_Fetching_Applied_Jobs_No_Applied_Jobs() throws Exception {
        setupSubscriptions();
        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .param("variant","applied")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(0)
        );
    }

    void setupAppliedJobs() throws Exception {
        mockMvc.perform(
                get("/api/jobs/mark-applied")
                        .param("job_id", "test-job-id-0")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        );
    }

    @Test
    void getJobs_When_Fetching_Applied_Jobs_With_Applied_Jobs() throws Exception {
        setupAppliedJobs();
        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .param("variant","applied")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(1)
        ).andExpect(
                jsonPath("$.data[0].title").value("Test Job 0")
        );
    }

    @Test
    void markApplied() {
        try{
            setupAppliedJobs();
        }catch (Exception e){
            throw new RuntimeException("Failed to mark job as applied", e);
        }
    }

    @Test
    void unmarkApplied() throws Exception {
        markApplied();
        mockMvc.perform(
                get("/api/jobs/unmark-applied")
                        .param("job_id", "test-job-id-0")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        );
    }

    @Test
    void updateJobApplicationStatus() throws Exception {
        setupAppliedJobs();
        mockMvc.perform(
                get("/api/jobs/update-job-application-status")
                        .param("job_id", "test-job-id-0")
                        .param("status", "REJECTED")
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.message").value("Job application status updated successfully")
        ).andExpect(
                jsonPath("$.data").value("REJECTED")
        );
    }

    @Test
    void postComment() throws Exception {
        when(jobCommentRepository.set(any())).thenReturn(JobComment.fromSimpleJobCommentModel(createTestComment()));
        SimpleUserModel user = userDataPort.getUserByEmail("test@gmail.com");
        mockMvc.perform(
                post("/api/jobs/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "jobId": "test-job-id-0",
                                    "comment": "This is a test comment"
                                }
                                """)
                        .header("Authorization", "Bearer "+generateToken())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.message").value("Comment added successfully")
        ).andExpect(
                jsonPath("$.data.comment").value("This is a test comment")
        ).andExpect(
                jsonPath("$.data.jobId").value("test-job-id-0")
        ).andExpect(
                jsonPath("$.data.userId").value(user.getId())
        );
    }

    SimpleJobCommentModel createTestComment() {
        SimpleUserModel simpleUserModel = userDataPort.getUserByEmail("test@gmail.com");
        return SimpleJobCommentModel.builder()
                .jobId("test-job-id-0")
                .comment("This is a test comment")
                .user(simpleUserModel)
                .userId(userRepository.findByEmail("test@gmail.com").getId())
                .build();
    }

    @MockitoBean
    JobCommentRepository jobCommentRepository;
    @Test
    void getComment() {
        try {
            when(jobCommentRepository.findByJobId(anyString(),anyLong(),anyInt())).thenReturn(List.of(JobComment.fromSimpleJobCommentModel(createTestComment())));
            mockMvc.perform(
                    get("/api/jobs/comment")
                            .param("job_id", "test-job-id-0")
                            .header("Authorization", "Bearer "+generateToken())
            ).andExpect(
                    jsonPath("$.success").value(true)
            ).andExpect(
                    jsonPath("$.data.length()").value(1)
            ).andExpect(
                    jsonPath("$.data[0].comment").value("This is a test comment")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get job comment", e);
        }
    }


    @Test
    void getJobs_When_InvalidPageParameter_ShouldThrowIllegalArgumentException() throws Exception {
        mockMvc.perform(
                get("/api/jobs")
                        .param("page", "-1")
                        .param("limit", "10")
                        .param("variant", "all")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void getJobs_When_LimitExceeds50_ShouldThrowIllegalArgumentException() throws Exception {
        mockMvc.perform(
                get("/api/jobs")
                        .param("page", "0")
                        .param("limit", "51")
                        .param("variant", "all")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void getJobs_When_NoAuthorizationHeader_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/jobs")
                        .param("limit", "10")
                        .param("variant", "all")
        ).andExpect(
                status().isForbidden()
        );
    }

    @Test
    void markApplied_When_JobIdDoesNotExist_ShouldThrowException() throws Exception {
        mockMvc.perform(
                get("/api/jobs/mark-applied")
                        .param("job_id", "non-existent-job-id")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void unmarkApplied_When_JobIdDoesNotExist_ShouldThrowException() throws Exception {
        mockMvc.perform(
                get("/api/jobs/unmark-applied")
                        .param("job_id", "non-existent-job-id")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void updateJobApplicationStatus_When_InvalidStatus_ShouldThrowException() throws Exception {
        setupAppliedJobs();
        mockMvc.perform(
                get("/api/jobs/update-job-application-status")
                        .param("job_id", "test-job-id-0")
                        .param("status", "INVALID_STATUS")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void updateJobApplicationStatus_When_JobIdDoesNotExist_ShouldThrowException() throws Exception {
        mockMvc.perform(
                get("/api/jobs/update-job-application-status")
                        .param("job_id", "non-existent-job-id")
                        .param("status", "REJECTED")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void postComment_When_JobIdMissing_ShouldThrowException() throws Exception {
        mockMvc.perform(
                post("/api/jobs/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "comment": "This is a test comment"
                            }
                            """)
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void postComment_When_CommentMissing_ShouldThrowException() throws Exception {
        mockMvc.perform(
                post("/api/jobs/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "jobId": "test-job-id-0"
                            }
                            """)
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void getComment_When_JobIdMissing_ShouldThrowException() throws Exception {
        mockMvc.perform(
                get("/api/jobs/comment")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @Test
    void getComment_When_NegativeLimit_ShouldThrowException() throws Exception {
        mockMvc.perform(
                get("/api/jobs/comment")
                        .param("job_id", "test-job-id-0")
                        .param("limit", "-1")
                        .header("Authorization", "Bearer " + generateToken())
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                jsonPath("$.success").value(false)
        );
    }

    @AfterEach
    void tearDown() {
        testUtils.clearDatabase();
    }
}