package com.iishanto.jobhunterbackend.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SimpleCalculatedResumeStrengthModel {
    private int score;
    private String reasoning;
    private List<String> improvementSuggestions;
}
