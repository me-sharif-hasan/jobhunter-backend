package com.iishanto.jobhunterbackend.domain.model.values;

public enum JobApplicationStatus {
    DEFAULT("DEFAULT"),
    APPLIED("APPLIED"),
    CALLED("CALLED"),
    INTERVIEWED("INTERVIEWED"),
    OFFERED("OFFERED"),
    REJECTED("REJECTED"),
    ACCEPTED("ACCEPTED"),
    WITHDRAWN("WITHDRAWN"),
    ARCHIVED("ARCHIVED"),
    UNAPPLIED("UNAPPLIED"),
    SHORTLISTED("SHORTLISTED");

    public final String label;
    JobApplicationStatus(String label) {
        this.label = label;
    }
}
