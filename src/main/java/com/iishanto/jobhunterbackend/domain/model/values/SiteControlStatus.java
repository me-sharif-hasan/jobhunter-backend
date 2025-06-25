package com.iishanto.jobhunterbackend.domain.model.values;

public enum SiteControlStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED"),
    ARCHIVED("ARCHIVED"),
    SUSPENDED("SUSPENDED"),
    MAINTENANCE("MAINTENANCE"),
    ERROR("ERROR");

    public final String label;
    SiteControlStatus(String label) {
        this.label = label;
    }
}
