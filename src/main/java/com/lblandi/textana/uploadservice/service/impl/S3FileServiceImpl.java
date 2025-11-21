package com.lblandi.textana.uploadservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.lblandi.textana.uploadservice.exception.FileUploadingException;
import com.lblandi.textana.uploadservice.response.FileUploadResponse;
import com.lblandi.textana.uploadservice.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class S3FileServiceImpl implements FileService {
    private final AmazonS3 s3Client;

    @Value("${textana.aws.bucket}")
    private String bucket;

    public S3FileServiceImpl(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public FileUploadResponse saveFile(MultipartFile file) {
        validateFile(file);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        ObjectMetadata metadata = createObjectMetadata(file);

        try {
            log.info("Saving file '{}' with identifier '{}'", file.getOriginalFilename(), uuid);
            s3Client.putObject(new PutObjectRequest(bucket, uuid, file.getInputStream(), metadata));
            return buildResponse(uuid, file.getOriginalFilename());

        } catch (IOException e) {
            return buildResponse(uuid, file.getOriginalFilename());
        }
    }

    private FileUploadResponse buildResponse(String uuid, String fileName) {
        return FileUploadResponse.builder()
                .trackingUuid(uuid)
                .fileName(fileName)
                .build();
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileUploadingException("The file to be uploaded is empty");
        }

        long maxSizeInBytes = 10 * (1024 * 1024L);
        if (file.getSize() > maxSizeInBytes) {
            throw new FileUploadingException("The file to be uploaded exceeds the maximum size of 10 MB");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.endsWith(".txt")) {
            throw new FileUploadingException("The file to be uploaded is not a text file");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equalsIgnoreCase("text/plain")) {
            throw new FileUploadingException("The file to be uploaded is not a text file");
        }
    }

    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }
}
