package com.iishanto.jobhunterbackend.domain.adapter;

public interface AiAdapter{
    <T>
    T getPromptResponse(String prompt,Class <T> responseType);
}
