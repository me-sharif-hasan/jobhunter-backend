package com.iishanto.jobhunterbackend.domain.model.values;

public enum IndexingStrategyNames {
    HYBRID("HYBRID"),
    MANUAL("MANUAL"),
    AI("AI"),;
    private final String label;
    IndexingStrategyNames(String label) {
        this.label = label;
    }
}
