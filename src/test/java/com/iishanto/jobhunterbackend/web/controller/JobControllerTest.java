package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.config.security.JwtUtil;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.service.UserService;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.ports.database.UserDataPort;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.hamcrest.Matchers;
import static org.hamcrest.Matchers.*;


import java.sql.Timestamp;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
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
    JobController jobController;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    UserService getUserUseCase;

    @MockitoBean
    UserDataPort userDataPort;

    @Autowired
    JobsRepository jobsRepository;
    @Autowired
    SiteRepository siteRepository;

    @BeforeEach
    void setUpAuthentication(){
        setupJobs();
        SimpleUserModel user = SimpleUserModel.builder()
                .id(1L)
                .email("test@gmail.com")
                .name("Test")
                .build();
        when(jwtUtil.getUsernameFromToken(any())).thenReturn("test@gmail.com");
        when(userDataPort.getLoggedInUser()).thenReturn(user);
        when(getUserUseCase.getUserByEmail(any())).thenReturn(user);
    }

    void setupJobs(){
        Site site = new Site();
        site.setName("Test Site");
        site.setJobListPageUrl("https://example.com");
        site.setDescription("This is a test site for job postings.");
        site = siteRepository.save(site);

        if(jobsRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                jobsRepository.save(Jobs.fromSimpleJobModel(
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
                ));
            }
        }
    }

    @Test
    void refreshJobs() {
        jobController.refreshJobs();
    }

    @Test
    void testRefreshJobs() {
    }

    @Test
    void getJobs_When_Fetching_All_Jobs() throws Exception {

        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .header("Authorization", "Bearer token.dummy")
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(10)
        ).andExpect(
                jsonPath("$.data[*].company").value(everyItem(is("Test Site")))
        );
    }

    @Test
    void markApplied() {
    }

    @Test
    void unmarkApplied() {
    }

    @Test
    void getAppliedJobs() {
    }

    @Test
    void updateJobApplicationStatus() {
    }

    @Test
    void postComment() {
    }

    @Test
    void getComment() {
    }
}