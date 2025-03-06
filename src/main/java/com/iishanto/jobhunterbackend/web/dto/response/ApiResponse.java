package com.iishanto.jobhunterbackend.web.dto.response;

import lombok.Data;

@Data
public class ApiResponse {
    private boolean success;
    private Object data;
    private String message;

    public ApiResponse(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }
}
