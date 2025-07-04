package com.iishanto.jobhunterbackend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
