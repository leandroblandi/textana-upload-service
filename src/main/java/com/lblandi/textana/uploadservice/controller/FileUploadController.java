package com.lblandi.textana.uploadservice.controller;

import com.lblandi.textana.uploadservice.response.ApiResponse;
import com.lblandi.textana.uploadservice.response.FileUploadResponse;
import com.lblandi.textana.uploadservice.service.FileService;
import com.lblandi.textana.uploadservice.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/upload")
public class FileUploadController {
    private final FileService fileService;
    private final QueueService queueService;

    public FileUploadController(FileService fileService, QueueService queueService) {
        this.fileService = fileService;
        this.queueService = queueService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(@RequestPart(name = "file") MultipartFile file) {
        FileUploadResponse response = fileService.saveFile(file);
        queueService.sendFileIdentifier(response.getTrackingUuid());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
