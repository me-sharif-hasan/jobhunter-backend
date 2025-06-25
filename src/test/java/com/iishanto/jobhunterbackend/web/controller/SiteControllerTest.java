package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import com.iishanto.jobhunterbackend.domain.usecase.AddSiteUseCase;
import com.iishanto.jobhunterbackend.infrastructure.crawler.WebCrawler;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import com.iishanto.jobhunterbackend.testutils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SiteControllerTest {

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        TestUtils.setupMySQL(registry);
    }

    @Autowired
    TestUtils testUtils;

    String jwtToken;
    @BeforeEach
    void setUp() {
        jwtToken=testUtils.setupAuthentication();
    }

    @Test
    void addSite() {
    }

    @Test
    void getSites() {
    }

    @MockitoBean
    WebCrawler webCrawler;

    void setupWebCrawler() throws IOException {
        String htmlFile = this.getClass().getResource("/test/sample-html-bs.html").getFile();
        htmlFile = FileUtils.readFileToString(new File(htmlFile), "UTF-8");
         when(webCrawler.getRawHtml(anyString())).thenReturn(htmlFile);
         when(webCrawler.getHtml(anyString())).thenReturn(htmlFile);
    }


    @Autowired
    AddSiteUseCase addSiteUseCase;

    @Test
    void reviewPersonalSite() throws IOException {
        setupWebCrawler();
        SimpleSiteModel site = addSiteUseCase.reviewSite("https://www.example.com/careers", "https://www.example.com");
        assertNotNull(site);
        assertEquals("Brain Station 23 | Easy Solution For The Job Recruitment", site.getName());
        assertEquals("https://www.example.com", site.getHomepage());
        assertEquals("https://www.example.com/careers", site.getJobListPageUrl());
    }
}