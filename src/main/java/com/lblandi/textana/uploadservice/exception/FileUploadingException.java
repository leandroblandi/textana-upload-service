package com.lblandi.textana.uploadservice.exception;

import java.io.Serial;

public class FileUploadingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FileUploadingException(String details) {
        super("Error while uploading file. Details: %s".formatted(details));
    }
}
