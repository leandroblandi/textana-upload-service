package com.lblandi.textana.uploadservice.service.impl;

import com.lblandi.textana.uploadservice.service.QueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
public class SqsService implements QueueService {
    private final SqsClient sqsClient;

    @Value( "${textana.sqs.queue.name}")
    private String queueUrl;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @Override
    public void sendFileIdentifier(String fileIdentifier) {
        log.info("Sending file identifier '{}' to SQS queue", fileIdentifier);

        try {

            // build send message request
            SendMessageRequest request = SendMessageRequest.builder()
                    .messageBody(fileIdentifier)
                    .queueUrl(queueUrl)
                    .build();

            // perform send
            sqsClient.sendMessage(request);
        } catch (Exception e) {
            log.error("Error while sending file identifier to SQS queue", e);
            throw e;
        }
    }
}
