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
        // ensure the file is valid
        validateFile(file);

        // generate a unique identifier for the file
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // build object metadata
        ObjectMetadata metadata = createObjectMetadata(file);

        try {
            // build put object request
            PutObjectRequest request = new PutObjectRequest(bucket, uuid, file.getInputStream(), metadata);
            log.info("Saving file '{}' with identifier '{}'", file.getOriginalFilename(), uuid);

            // perform operation and return the response
            s3Client.putObject(request);
            return buildResponse(uuid, file.getOriginalFilename());

        } catch (IOException e) {
            return buildResponse(uuid, file.getOriginalFilename());
        }
    }

    /**
     * Builds and returns a {@link FileUploadResponse} object with the specified UUID and file name.
     *
     * @param uuid the unique identifier for the file being uploaded
     * @param fileName the original name of the uploaded file
     * @return a {@link FileUploadResponse} containing the file tracking UUID and file name
     */
    private FileUploadResponse buildResponse(String uuid, String fileName) {
        return FileUploadResponse.builder()
                .trackingUuid(uuid)
                .fileName(fileName)
                .build();
    }

    /**
     * Validates the given file to ensure it meets specific requirements for upload.
     *
     * The validation checks include:
     * - Ensuring the file is not null or empty.
     * - Restricting the file size to a maximum of 10 MB.
     * - Verifying the file name ends with ".txt".
     * - Confirming the file content type is "text/plain".
     *
     * If any of these checks fail, a {@link FileUploadingException} is thrown.
     *
     * @param file the file to be validated, represented as a {@link MultipartFile}
     * @throws FileUploadingException if the file is null, empty, exceeds the size limit,
     *         has an invalid extension, or has an unsupported content type.
     */
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

    /**
     * Creates and returns an {@link ObjectMetadata} object containing metadata information
     * derived from the specified {@link MultipartFile}.
     *
     * @param file the file from which metadata such as content type and size will be extracted
     * @return an {@link ObjectMetadata} object populated with file metadata
     */
    private ObjectMetadata createObjectMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }
}
