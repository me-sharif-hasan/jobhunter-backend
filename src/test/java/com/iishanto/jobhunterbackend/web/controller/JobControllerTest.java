package com.iishanto.jobhunterbackend.web.controller;

import com.iishanto.jobhunterbackend.config.security.JwtUtil;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.domain.service.UserService;
import com.iishanto.jobhunterbackend.infrastructure.ports.database.UserDataPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class JobControllerTest {

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

    @MockitoBean
    UserJobAccessDa


    @BeforeEach
    void setUpAuthentication(){
        SimpleUserModel user = SimpleUserModel.builder()
                .id(1L)
                .email("test@gmail.com")
                .name("Test")
                .build();
        when(jwtUtil.getUsernameFromToken(any())).thenReturn("test@gmail.com");
        when(userDataPort.getLoggedInUser()).thenReturn(user);
        when(getUserUseCase.getUserByEmail(any())).thenReturn(user);
    }

    @Test
    void refreshJobs() {
        jobController.refreshJobs();
    }

    @Test
    void testRefreshJobs() {
    }

    @Test
    void getJobs() throws Exception {

        mockMvc.perform(
                get("/api/jobs")
                        .param("limit","10")
                        .header("Authorization", "Bearer token.dummy")
        ).andExpect(
                jsonPath("$.success").value(true)
        ).andExpect(
                jsonPath("$.data.length()").value(10)
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