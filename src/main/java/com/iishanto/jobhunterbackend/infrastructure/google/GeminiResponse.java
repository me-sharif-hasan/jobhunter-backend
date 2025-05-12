package com.iishanto.jobhunterbackend.infrastructure.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class GeminiResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

    @Data
    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static class Candidate {
        private Content content;
        private String finishReason;
        private String avgLogprobs;
    }

    @Data
    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @Data
    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static class Part {
        private String text;
    }

    @Data
    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static class UsageMetadata {
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;
        private List<TokensDetail> promptTokensDetails;
        private List<TokensDetail> candidatesTokensDetails;
    }

    @Data
    @JsonIgnoreProperties(
            ignoreUnknown = true
    )
    public static class TokensDetail {
        private String modality;
        private int tokenCount;
    }
}
