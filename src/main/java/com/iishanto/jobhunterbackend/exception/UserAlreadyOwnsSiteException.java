package com.iishanto.jobhunterbackend.exception;

import lombok.Getter;

@Getter
public class UserAlreadyOwnsSiteException extends RuntimeException {
    private Long siteId;
    private Long id;
    public UserAlreadyOwnsSiteException(String message, Long siteId, Long id) {
        super(message + " Site ID: " + siteId + ", User ID: " + id);
        this.siteId = siteId;
        this.id = id;
    }
}
