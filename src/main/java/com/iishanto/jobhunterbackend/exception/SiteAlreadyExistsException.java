package com.iishanto.jobhunterbackend.exception;

import lombok.Getter;

@Getter
public class SiteAlreadyExistsException extends RuntimeException {
    private Long existingSiteId;
    public SiteAlreadyExistsException(String s) {
        super(s);
    }
    public SiteAlreadyExistsException(String s,Long existingSiteId) {
        super(s + " Existing site ID: " + existingSiteId);
        this.existingSiteId = existingSiteId;
    }

}
