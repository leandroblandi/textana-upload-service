package com.lblandi.textana.uploadservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponse {
    @JsonProperty("file_tracking_uuid")
    private String trackingUuid;

    @JsonProperty("file_name")
    private String fileName;
}
