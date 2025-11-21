package com.lblandi.textana.uploadservice.controller;

import com.lblandi.textana.uploadservice.exception.FileUploadingException;
import com.lblandi.textana.uploadservice.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(FileUploadingException.class)
    public ResponseEntity<ApiResponse<String>> handleException(FileUploadingException e) {
        ApiResponse<String> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleException(RuntimeException e) {
        ApiResponse<String> response = ApiResponse.error("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
