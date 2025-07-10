package com.iishanto.jobhunterbackend.domain.model.values;

public enum ClickIntention {
    NAVIGATE("navigate"),ACTION("action");
    public final String name;
    ClickIntention(String name) {
        this.name = name;
    }
}
