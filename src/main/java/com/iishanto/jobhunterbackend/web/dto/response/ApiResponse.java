package com.iishanto.jobhunterbackend.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ApiResponse {
    private boolean success;
    private Object data;
    private long totalRecords;
    private String message;

    public ApiResponse(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        if(data instanceof List){
            this.totalRecords = ((List<?>) data).size();
        } else if(data instanceof String){
            this.totalRecords = 1;
        } else {
            this.totalRecords = 0;
        }
    }
}
