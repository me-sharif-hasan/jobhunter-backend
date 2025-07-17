package com.iishanto.jobhunterbackend.infrastructure.ports.google;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iishanto.jobhunterbackend.domain.adapter.AiAdapter;
import com.iishanto.jobhunterbackend.infrastructure.google.GeminiClient;
import com.iishanto.jobhunterbackend.infrastructure.google.GeminiPromptLibrary;
import com.iishanto.jobhunterbackend.infrastructure.google.GeminiResponse;
import com.iishanto.jobhunterbackend.utils.HunterUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class GeminiParserPort implements AiAdapter{
    private final ObjectMapper objectMapper;
    private final GeminiClient geminiClient;
    @Override
    public <T> T getPromptResponse(String prompt,Class<T> clazz) {
        String escapedPrompt = HunterUtils.sanitizeMarkdown(prompt);
        GeminiClient.GeminiPrompt geminiPrompt = GeminiClient.GeminiPrompt.builder()
                .message(escapedPrompt)
                .temperature(0)
                .build();
        String geminiResponse = geminiClient.getResponse(geminiPrompt.getPromptTemplate(GeminiPromptLibrary.PromptType.CV_STRENGTH));
        if(StringUtils.isNotBlank(geminiResponse)) {
            try {
                GeminiResponse response = objectMapper.readValue(geminiResponse, GeminiResponse.class);
                if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                    String text = response.getCandidates().get(0).getContent().getParts().get(0).getText();
                    String json=HunterUtils.sanitizeJsonString(text.substring(7,text.length()-3)).trim();
                    T result = objectMapper.readValue(json, clazz);
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
