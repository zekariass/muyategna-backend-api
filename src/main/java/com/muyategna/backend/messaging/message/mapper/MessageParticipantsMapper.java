package com.muyategna.backend.messaging.message.mapper;

import com.muyategna.backend.messaging.message.dto.MessageParticipantListViewDto;
import com.muyategna.backend.messaging.message.entity.MessageParticipant;

import java.util.List;

public final class MessageParticipantsMapper {
    public static MessageParticipantListViewDto toDto(MessageParticipant messageParticipant) {
        if (messageParticipant == null) {
            return null;
        }

        MessageParticipantListViewDto dto = new MessageParticipantListViewDto();
        dto.setId(messageParticipant.getId());
        dto.setParticipantId(messageParticipant.getParticipantId());
        dto.setParticipantType(messageParticipant.getParticipantType());
        dto.setUnreadCount(messageParticipant.getUnreadCount());
        if (messageParticipant.getUserProfile() != null) {
            dto.setUserProfileId(messageParticipant.getUserProfile().getId());
        }

        dto.setThreadId(messageParticipant.getThread().getId());

        return dto;
    }


    public static List<MessageParticipantListViewDto> toListDto(List<MessageParticipant> messageParticipants) {
        return messageParticipants.stream().map(MessageParticipantsMapper::toDto).toList();
    }
}
