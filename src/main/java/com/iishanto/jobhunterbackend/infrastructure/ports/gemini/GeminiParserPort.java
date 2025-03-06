package com.iishanto.jobhunterbackend.infrastructure.ports.gemini;

import com.iishanto.jobhunterbackend.domain.adapter.JobParserAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminiParserPort implements JobParserAdapter {
    @Override
    public List<SimpleJobModel> parse(String url) {
        return List.of();
    }
}
