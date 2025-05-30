package com.iishanto.jobhunterbackend.infrastructure.google;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GeminiClientTest {
    @Autowired
    GeminiClient geminiClient;
    @Test
    void getJobListingPromptFromUrl() {
        GeminiClient.GeminiPrompt prompt = geminiClient.getJobListingPromptFromUrl("https://www.linkedin.com/posts/sazim_%F0%9D%97%AA%F0%9D%97%B2-%F0%9D%97%AE%F0%9D%97%BF%F0%9D%97%B2-%F0%9D%97%B5%F0%9D%97%B6%F0%9D%97%BF%F0%9D%97%B6%F0%9D%97%BB%F0%9D%97%B4-%F0%9D%97%A7%F0%9D%97%B2%F0%9D%97%B0%F0%9D%97%B5%F0%9D%97%BB%F0%9D%97%B6%F0%9D%97%B0%F0%9D%97%AE%F0%9D%97%B9-activity-7261684935801937921-8ENa/");
        assertNotNull(prompt);
        System.out.println(prompt.getPromptTemplate(GeminiPromptLibrary.PromptType.JOB_LIST));
        List<SimpleJobModel> jobModels = geminiClient.getJsonResponseOfJobs(prompt);
        assertNotNull(jobModels);
        System.out.println(jobModels);
    }
}