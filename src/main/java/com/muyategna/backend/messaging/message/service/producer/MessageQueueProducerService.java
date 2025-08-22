package com.muyategna.backend.messaging.message.service.producer;

import com.muyategna.backend.messaging.message.entity.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageQueueProducerService {

    void enqueueMessageAttachmentUpload(Message message, List<MultipartFile> files);
}
