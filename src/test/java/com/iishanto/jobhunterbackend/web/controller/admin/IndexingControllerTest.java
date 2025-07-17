package com.iishanto.jobhunterbackend.web.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.iishanto.jobhunterbackend.infrastructure.database.Site;
import com.iishanto.jobhunterbackend.infrastructure.repository.IndexingStrategyRepository;
import com.iishanto.jobhunterbackend.testutils.TestDataFactory;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.argThat;

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
        testUtils.clearDatabase();
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
                    "type":"MANUAL",
                    "processFlow":[
                        {
                            "operation":"find",
                            "selector":"//*[@id=\\"section-opl-job-circular\\"]/div/div[2]/a",
                            "childPipelines":[
                                {
                                    "operation":"click"
                                },
                                {
                                    "operation":"map",
                                    "selector":"/html/body/main/article/div[1]/section/div/div/div[1]/h1",
                                    "attribute":"title"
                                },
                                {
                                    "operation":"map",
                                    "javaScript":"return window.location.href;",
                                    "attribute":"jobId"
                                },
                                {
                                    "operation":"save"
                                },
                                {
                                    "operation":"back"
                                }
                            ],
                            "metaFieldsMapping":[
                                {
                                    "operation":"map",
                                    "selector":"./div/div[1]/div[2]/p[4]/span",
                                    "attribute":"jobLastDate"
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

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(
                    wireMockConfig().dynamicPort()
                            .fileSource(new ClasspathFileSource(""))
            )
            .build();

    @Test
    void validateJobIndexStrategy_OPL() throws Exception {
        String careerPageHtml = new String(
                Files.readAllBytes(
                        Paths.get(Objects.requireNonNull(
                                this.getClass().getResource("/test/opl/sample-html-opl.html")
                        ).toURI())
                )
        );

        String jobDetailsHtml = new String(
                Files.readAllBytes(
                        Paths.get(Objects.requireNonNull(
                                this.getClass().getResource("/test/opl/sample-job-page.html")
                        ).toURI())
                )
        );
        wireMock.stubFor(get(urlEqualTo("/Career/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(careerPageHtml)));

        // Stub for URLs starting with /Apply
        wireMock.stubFor(get(urlPathMatching("/Apply.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(jobDetailsHtml)));

        Site site = testDataFactory.createSite(wireMock.getRuntimeInfo().getHttpBaseUrl(), wireMock.getRuntimeInfo().getHttpBaseUrl()+"/Career/");
        assertNotNull(site, "Site should be created successfully");
        assertNotNull(site.getId(), "Site ID should not be null");

        MvcResult theResult = mockMvc.perform(
                post(
                        "/admin/indexing/validate-strategy?site_id=%d".formatted(site.getId())
                )
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .content(getStrategyJson())
        )
                .andExpect(
                        result -> assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value())
                )
                .andExpect(
                        jsonPath("$.success").value(true)
                )
                .andExpect(
                        jsonPath("$.data").isArray()
                )
                .andExpect(
                        jsonPath("$.data.length()").value(6)
                )
                .andReturn();
    }


    private String getChaldalJson(){
        return """
                {
                                    "processFlow": [
                                        {
                                             "operation":"find",
                                             "selector":"//*[@id=\\"career\\"]/div/div[2]/a | //*[@id=\\"career\\"]/div/div[3]/div/a",
                                             "childPipelines":[
                                                 {
                                                     "clickIntent":"ACTION",
                                                     "operation":"click"
                                                 },
                                                 {
                                                     "operation":"map",
                                                     "selector":"./../div",
                                                     "attribute":"jobDescription"
                                                 },
                                                 {
                                                     "operation":"map",
                                                     "selector":"./span",
                                                     "attribute":"title"
                                                 },
                                                 {
                                                     "operation":"map",
                                                     "selector":"./p",
                                                     "attribute":"location"
                                                 },
                                                 {
                                                     "operation":"map",
                                                     "javaScript": "return window.location.href + '%title%'.trim().replace(\\/\\\\s+\\/g, '_');",
                                                     "attribute":"jobId"
                                                 },
                                                 {
                                                     "operation":"save"
                                                 }
                                             ]
                                        }
                                    ]
                                }
                """;
    }
}