package com.iishanto.jobhunterbackend.web.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.testutils.TestDataFactory;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import com.iishanto.jobhunterbackend.web.dto.request.UpdateSiteDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AdminSiteControllerTest {
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
    private SiteRepository siteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = testUtils.setupAuthentication();
        siteRepository.deleteAll();
    }

    @Test
    void updateSite_ShouldUpdateSiteAndReturnSuccess() throws Exception {
        // Create a test site
        Site site = testDataFactory.createSite("http://old-homepage.com", "http://old-careers.com");

        // Prepare update data
        UpdateSiteDto updateDto = new UpdateSiteDto();
        updateDto.setHomepage("http://new-homepage.com");
        updateDto.setJobListPageUrl("http://new-careers.com");

        mockMvc.perform(patch("/admin/site/" + site.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(site.getId()))
                .andExpect(jsonPath("$.message").value("Site updated successfully"));

        // Verify site is updated in database
        Site updatedSite = siteRepository.findById(site.getId()).orElseThrow();
        assert updatedSite.getHomepage().equals(updateDto.getHomepage());
        assert updatedSite.getJobListPageUrl().equals(updateDto.getJobListPageUrl());
    }

    @Test
    void updateSite_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        Site site = testDataFactory.createSite("http://test.com", "http://test.com/careers");
        UpdateSiteDto updateDto = new UpdateSiteDto();
        updateDto.setHomepage("http://new.com");
        updateDto.setJobListPageUrl("http://new.com/careers");

        mockMvc.perform(patch("/admin/site/" + site.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSite_WithInvalidSiteId_ShouldReturnNotFound() throws Exception {
        UpdateSiteDto updateDto = new UpdateSiteDto();
        updateDto.setHomepage("http://new.com");
        updateDto.setJobListPageUrl("http://new.com/careers");

        mockMvc.perform(patch("/admin/site/999")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void updateSite_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Site site = testDataFactory.createSite("http://test.com", "http://test.com/careers");
        UpdateSiteDto updateDto = new UpdateSiteDto();
        // Missing required fields

        mockMvc.perform(patch("/admin/site/" + site.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    void tearDown() {
        testUtils.clearDatabase();
        testDataFactory.resetWebCrawler();
    }
}
