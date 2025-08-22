package com.muyategna.backend.messaging.message.service;

import com.muyategna.backend.messaging.message.dto.MessageAttachmentUploadQueueMessage;
import com.muyategna.backend.messaging.message.dto.MessageCreateDto;
import com.muyategna.backend.messaging.message.dto.MessageListViewDto;
import com.rabbitmq.client.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    MessageListViewDto createMessage(MessageCreateDto messageCreateDto);

    Page<MessageListViewDto> getPagedMessagesByThreadId(Long threadId, Pageable pageable);

    void uploadAttachment(MessageAttachmentUploadQueueMessage queueMessage,
                          org.springframework.amqp.core.Message message,
                          Channel channel) throws Exception;
}
