package com.iishanto.jobhunterbackend.infrastructure.aop;

import com.iishanto.jobhunterbackend.web.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class ResponseAspect {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {
        exception.printStackTrace();

        return new ResponseEntity<>(
                new ApiResponse(false, null, "Error occurred: " + exception.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
