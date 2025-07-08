package com.iishanto.jobhunterbackend.web.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.IndexingStrategyRepository;
import com.iishanto.jobhunterbackend.testutils.TestDataFactory;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;



import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class IndexingControllerTest {
    String jwtToken;
    @Autowired
    TestUtils testUtils;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestDataFactory testDataFactory;
    @Autowired
    IndexingStrategyRepository indexingStrategyRepository;
    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        TestUtils.setupMySQL(registry);
    }

    @BeforeEach
    void setUp() {
        jwtToken = testUtils.setupAuthentication();
        assertNotNull(jwtToken, "JWT Token should not be null");
    }

    @AfterEach
    void tearDown() {
        testUtils.clearDatabase();
    }

    @Test
    void refreshJobIndex() {
    }

    @Test
    void saveJobIndexStrategy() throws Exception {
        assertNotNull(jwtToken, "JWT Token should not be null");
        testDataFactory.setupWebCrawler();
        Site site = testDataFactory.createSite("https://www.example.com", "https://www.example.com/careers");
        assertNotNull(site, "Site should be created successfully");
        assertNotNull(site.getId(), "Site ID should not be null");
        assertEquals(1L, site.getId(), "Site ID should be 1");

        MvcResult theResult = mockMvc.perform(
                post(
                        "/admin/indexing/save-strategy?site_id=%d".formatted(site.getId())
                )
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .content(getStrategyJson())
        ).andExpect(
                result -> assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value())
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data").isNumber()
        ).andReturn();

        String responseContent = theResult.getResponse().getContentAsString();
        assertNotNull(responseContent, "Response content should not be null");
        ApiResponse apiResponse = objectMapper.readValue(responseContent, ApiResponse.class);
        assertNotNull(apiResponse, "API Response should not be null");
        Integer savedId = (Integer) apiResponse.getData();
        assertNotNull(savedId, "Saved ID should not be null");
        assertTrue(savedId > 0, "Saved ID should be greater than 0");
        assertTrue(indexingStrategyRepository.existsById(savedId.longValue()), "Indexing strategy should be saved in the database");
    }

    private String getStrategyJson() {
        return """
                {
                    "processFlow": [
                        {
                             "operation":"find",
                             "selector":"//mat-card/div/mat-card-actions/button",
                             "childPipelines":[
                                 {
                                     "operation":"click"
                                 },
                                 {
                                     "operation":"askAi"
                                 },
                                 {
                                     "operation":"save"
                                 },
                                 {
                                     "operation":"back"
                                 }
                             ]
                        }
                    ]
                }
                """;
    }

    private Long createSite(){
return 0l;
    }
}