package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.web.dto.request.AddSiteDto;
import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SiteControllerTest {
    @Autowired
    SiteController siteController;
    @Test
    void addSite() {
        String homepage="https://wppool.dev/";
        String jobListPageUrl="https://careers.wppool.dev/";
        AddSiteDto addSiteDto=AddSiteDto.builder()
                .homepage(homepage)
                .jobListPageUrl(jobListPageUrl)
                .build();
        ApiResponse response = siteController.addSite(addSiteDto);

        assertTrue(response.isSuccess());

    }
}