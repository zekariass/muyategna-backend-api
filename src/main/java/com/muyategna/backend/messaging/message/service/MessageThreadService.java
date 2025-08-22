package com.muyategna.backend.messaging.message.service;

import com.muyategna.backend.messaging.message.dto.MessageThreadCreateDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadDetailViewDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadListViewDto;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import com.muyategna.backend.messaging.message.repository.projection.MessageThreadProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageThreadService {
    MessageThreadListViewDto createMessageThread(MessageThreadCreateDto messageThreadCreateDto);

    MessageThreadDetailViewDto getMessageThreadDetailById(Long messageThreadId);

    Page<MessageThreadListViewDto> getAllMessageThreadsForServiceProvider(Long serviceProviderId, Pageable pageable);

    Page<MessageThreadListViewDto> getAllMessageThreadsForCustomer(Long customerId, Pageable pageable);

    Page<MessageThreadProjection> findDistinctThreadsByParticipant(Long participantId, ParticipantType participantType, Pageable pageable);

}
