package com.iishanto.jobhunterbackend.web.dto.request;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSiteDto {
    private String name;
    @NotBlank
    private String homepage;
    private String description;
    private String iconUrl;
    @NotBlank
    private String jobListPageUrl;
    private Timestamp lastCrawledAt;

    public SimpleSiteModel toDomain() {
        return new SimpleSiteModel(
                null,
                this.name,
                this.homepage,
                this.description,
                this.iconUrl,
                this.jobListPageUrl,
                lastCrawledAt
        );
    }
}
