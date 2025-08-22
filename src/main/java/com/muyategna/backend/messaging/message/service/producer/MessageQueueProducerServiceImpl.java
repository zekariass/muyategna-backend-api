package com.muyategna.backend.messaging.message.service.producer;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.messaging.message.dto.MessageAttachmentUploadQueueMessage;
import com.muyategna.backend.messaging.message.entity.Message;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class MessageQueueProducerServiceImpl implements MessageQueueProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageQueueProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enqueueMessageAttachmentUpload(Message message, List<MultipartFile> files) {

        try {
            for (MultipartFile file : files) {

                if (file == null || file.isEmpty()) {
                    log.warn("Proof of payment file is required");
                    continue;
                }

                String ext = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
                boolean extIsValid = Constants.IMAGE_EXTENSIONS.contains(ext) ||
                        Constants.VIDEO_EXTENSIONS.contains(ext) ||
                        Constants.DOCUMENT_EXTENSIONS.contains(ext);

                if (!extIsValid) {
                    log.warn("File extension is not valid the file type uploaded");
                    continue;
                }

                // Validate file size
                if (Constants.IMAGE_EXTENSIONS.contains(ext) && file.getSize() > Constants.IMAGE_MAX_SIZE) {
                    log.warn("File is too large. Maximum size for image files is " + Constants.IMAGE_MAX_SIZE / (1024 * 1024) + " MB");
                    continue;
                } else if (Constants.VIDEO_EXTENSIONS.contains(ext) && file.getSize() > Constants.VIDEO_MAX_SIZE) {
                    log.warn("File is too large. Maximum size for video files is " + Constants.VIDEO_MAX_SIZE / (1024 * 1024) + " MB");
                    continue;
                } else if (Constants.DOCUMENT_EXTENSIONS.contains(ext) && file.getSize() > Constants.DOCUMENT_MAX_SIZE) {
                    log.warn("File is too large. Maximum size for document files is " + Constants.DOCUMENT_MAX_SIZE / (1024 * 1024) + " MB");
                    continue;
                }

                // Construct QueueMessage
                MessageAttachmentUploadQueueMessage queueMessage = MessageAttachmentUploadQueueMessage.builder()
                        .messageId(message.getId())
                        .attachment(file.getBytes())
                        .originalFilename(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .extension(ext)
                        .newFilename(UUID.randomUUID().toString())
                        .build();

                QueueSpec queueSpec = QueueSpec.withBaseName(Constants.MESSAGE_ATTACHMENT_UPLOAD_QUEUE_BASE_NAME);
                rabbitTemplate.convertAndSend(
                        queueSpec.exchange(),
                        queueSpec.routingKey(),
                        queueMessage
                );
            }
        } catch (IOException e) {
            log.error("Failed to enqueue message attachment upload", e);
        }
    }


    private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            throw new IllegalArgumentException("Invalid file extension in filename: " + filename);
        }
        return filename.substring(idx + 1);
    }
}
