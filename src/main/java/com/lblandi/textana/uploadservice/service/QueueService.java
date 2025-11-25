package com.lblandi.textana.uploadservice.service;

/**
 * @author lblandi
 * @since 2025-11-25
 */
public interface QueueService {

    /**
     * Sends the specified file identifier to be processed or queued for further handling.
     *
     * @param fileIdentifier the unique identifier of the file to be sent
     */
    void sendFileIdentifier(String fileIdentifier);
}
