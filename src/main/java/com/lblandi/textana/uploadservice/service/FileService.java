package com.lblandi.textana.uploadservice.service;

import com.lblandi.textana.uploadservice.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lblandi
 * @since 2025-11-25
 */
public interface FileService {

    /**
     * Saves the given file and returns an UUID
     *
     * @param file the file to be saved, encapsulated in a MultipartFile object
     * @return a String representing the identifier of the saved file
     */
    FileUploadResponse saveFile(MultipartFile file);
}
