package com.muyategna.backend.messaging.message.mapper;

import com.muyategna.backend.messaging.message.dto.MessageDto;
import com.muyategna.backend.messaging.message.dto.MessageListViewDto;
import com.muyategna.backend.messaging.message.entity.Message;

import java.util.List;

public final class MessageMapper {
    public static MessageDto toDto(Message message) {
        if (message == null) {
            return null;
        }

        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSenderId());
        dto.setThreadId(message.getThread().getId());
        dto.setReplyToId(message.getReplyTo().getId());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());

        return dto;
    }


    public static MessageListViewDto toListViewDto(Message message) {
        if (message == null) {
            return null;
        }

        MessageListViewDto dto = new MessageListViewDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSenderId());
        dto.setThreadId(message.getThread().getId());

        if (message.getReplyTo() != null) {
            dto.setReplyToId(message.getReplyTo().getId());
        }

        dto.setAttachments(message.getAttachments());
        dto.setSenderType(message.getSenderType());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());

        return dto;
    }


    public static List<MessageDto> toListDto(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        return messages.stream()
                .map(MessageMapper::toDto)
                .toList();
    }
}
