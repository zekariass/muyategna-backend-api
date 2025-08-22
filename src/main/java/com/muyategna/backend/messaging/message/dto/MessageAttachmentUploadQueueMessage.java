package com.muyategna.backend.messaging.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageAttachmentUploadQueueMessage {
    private Long messageId;
    private byte[] attachment;
    private String originalFilename;
    private String newFilename;
    private String extension;
    private String contentType;
}
