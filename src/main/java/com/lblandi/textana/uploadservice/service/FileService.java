package com.lblandi.textana.uploadservice.service;

import com.lblandi.textana.uploadservice.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * Saves the given file and returns an UUID
     *
     * @param file the file to be saved, encapsulated in a MultipartFile object
     * @return a String representing the identifier of the saved file
     */
    public FileUploadResponse saveFile(MultipartFile file);
}
