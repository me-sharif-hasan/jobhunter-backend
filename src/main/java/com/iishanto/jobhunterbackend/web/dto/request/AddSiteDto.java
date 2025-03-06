package com.iishanto.jobhunterbackend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddSiteDto {
    public String name;
    @NotBlank
    public String homepage;
    public String description;
    public String iconUrl;
    @NotBlank
    public String jobListPageUrl;
    public String lastCrawledAt;
}
