package com.iishanto.jobhunterbackend.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleCalculatedResumeStrengthModel {
    private int score;
    private String reasoning;
}
