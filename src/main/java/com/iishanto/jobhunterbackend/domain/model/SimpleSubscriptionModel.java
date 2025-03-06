package com.iishanto.jobhunterbackend.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleSubscriptionModel {
    private Long id;
    private SimpleSiteModel site;
    private SimpleUserModel user;
    private boolean isActive;
}
