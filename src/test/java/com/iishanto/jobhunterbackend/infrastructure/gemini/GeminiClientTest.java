package com.iishanto.jobhunterbackend.infrastructure.gemini;

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
        GeminiClient.GeminiPrompt prompt = geminiClient.getJobListingPromptFromUrl("https://www.linkedin.com/company/exabyting/jobs/");
        assertNotNull(prompt);
        System.out.println(prompt.getPromptTemplate(GeminiPromptLibrary.PromptType.JOB_LIST));
        List<SimpleJobModel> jobModels = geminiClient.getJsonResponseOfJobs(prompt);
        assertNotNull(jobModels);
        System.out.println(jobModels);
    }
}