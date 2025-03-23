package com.iishanto.jobhunterbackend.web.dto.response.site;

import com.iishanto.jobhunterbackend.domain.model.SimpleSiteModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteResponseDto {
    private Long id;
    private String name;
    private String homepage;
    private String description;
    private String iconUrl;
    private String jobListPageUrl;
    private LocalDateTime lastCrawledAt;
    private boolean isSubscribed;

    public static SiteResponseDto fromSimpleSiteModel(SimpleSiteModel siteModel) {
        ZoneId userZoneId = getRequestTimeZone();
        LocalDateTime localLastCrawledAt = Optional.ofNullable(siteModel.getLastCrawledAt())
                .map(Timestamp::toInstant)
                .map(instant -> instant.atZone(ZoneId.of("UTC")).withZoneSameInstant(userZoneId).toLocalDateTime())
                .orElse(null);
        System.out.println(localLastCrawledAt+" "+userZoneId+" "+siteModel.getLastCrawledAt());

        return SiteResponseDto.builder()
                .id(siteModel.getId())
                .name(siteModel.getName())
                .homepage(siteModel.getHomepage())
                .description(siteModel.getDescription())
                .iconUrl(siteModel.getIconUrl())
                .jobListPageUrl(siteModel.getJobListPageUrl())
                .lastCrawledAt(localLastCrawledAt)
                .isSubscribed(siteModel.isSubscribed())
                .build();
    }

    private static ZoneId getRequestTimeZone() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return Optional.ofNullable(request.getHeader("Time-Zone"))
                .map(ZoneId::of)
                .orElse(ZoneId.systemDefault());
    }
}
