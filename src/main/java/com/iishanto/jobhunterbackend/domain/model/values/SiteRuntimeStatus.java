package com.iishanto.jobhunterbackend.domain.model.values;

public enum SiteRuntimeStatus {
    INDEXING("INDEXING"),
    IDLE("IDLE"),
    CRAWLING("CRAWLING"),
    ERROR("ERROR");
    public final String label;
    SiteRuntimeStatus(String label) {
        this.label = label;
    }
}
