package com.muyategna.backend.messaging.message.mapper;

import com.muyategna.backend.messaging.message.dto.MessageThreadDetailViewDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadListViewDto;
import com.muyategna.backend.messaging.message.entity.MessageParticipant;
import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.repository.projection.MessageThreadProjection;

import java.util.List;

public final class MessageThreadMapper {
    public static MessageThreadListViewDto toListViewDto(MessageThread messageThread) {

        if (messageThread == null) {
            return null;
        }
        MessageThreadListViewDto messageThreadListViewDto = new MessageThreadListViewDto();
        messageThreadListViewDto.setId(messageThread.getId());
        messageThreadListViewDto.setSubject(messageThread.getSubject());
        messageThreadListViewDto.setContextType(messageThread.getContextType());
        messageThreadListViewDto.setContextId(messageThread.getContextId());
        messageThreadListViewDto.setCustomerId(messageThread.getCustomer().getId());
        messageThreadListViewDto.setServiceProviderId(messageThread.getServiceProvider().getId());
        messageThreadListViewDto.setParticipants(MessageParticipantsMapper.toListDto(messageThread.getParticipants()));

        return messageThreadListViewDto;
    }


    public static MessageThreadDetailViewDto toDetailViewDto(MessageThread messageThread) {

        if (messageThread == null) {
            return null;
        }
        MessageThreadDetailViewDto messageThreadDetailViewDto = new MessageThreadDetailViewDto();
        messageThreadDetailViewDto.setId(messageThread.getId());
        messageThreadDetailViewDto.setSubject(messageThread.getSubject());
        messageThreadDetailViewDto.setContextType(messageThread.getContextType());
        messageThreadDetailViewDto.setContextId(messageThread.getContextId());
        messageThreadDetailViewDto.setCustomerId(messageThread.getCustomer().getId());
        messageThreadDetailViewDto.setServiceProviderId(messageThread.getServiceProvider().getId());
        messageThreadDetailViewDto.setParticipants(MessageParticipantsMapper.toListDto(messageThread.getParticipants()));
//        messageThreadDetailViewDto.setMessages(MessageMapper.toListDto(messageThread.getMessages()));

        return messageThreadDetailViewDto;
    }

    public static MessageThreadListViewDto toListViewDto(MessageThreadProjection mp, List<MessageParticipant> participants) {

        if (mp == null) {
            return null;
        }
        MessageThreadListViewDto messageThreadListViewDto = new MessageThreadListViewDto();
        messageThreadListViewDto.setId(mp.getId());
        messageThreadListViewDto.setSubject(mp.getSubject());
        messageThreadListViewDto.setContextType(mp.getContextType());
        messageThreadListViewDto.setContextId(mp.getContextId());
        messageThreadListViewDto.setParticipants(MessageParticipantsMapper.toListDto(participants));

        return messageThreadListViewDto;
    }


    public static List<MessageThreadListViewDto> toListViewDto(List<MessageThread> messageThreads) {
        return messageThreads.stream().map(MessageThreadMapper::toListViewDto).toList();
    }
}
