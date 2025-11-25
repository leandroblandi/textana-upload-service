package com.lblandi.textana.uploadservice.controller;

import com.lblandi.textana.uploadservice.exception.FileUploadingException;
import com.lblandi.textana.uploadservice.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

@RestControllerAdvice
public class ErrorController {

    // file uploading related exceptions
    @ExceptionHandler(FileUploadingException.class)
    public ResponseEntity<ApiResponse<String>> handleException(FileUploadingException e) {
        ApiResponse<String> response = ApiResponse.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // aws related exceptions
    @ExceptionHandler(AwsServiceException.class)
    public ResponseEntity<ApiResponse<String>> handleException(AwsServiceException e) {
        ApiResponse<String> response = ApiResponse.error("We are experiencing technical difficulties. Please try again later.");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    // another random exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleException(RuntimeException e) {
        ApiResponse<String> response = ApiResponse.error("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
