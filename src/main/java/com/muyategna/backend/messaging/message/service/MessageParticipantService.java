package com.muyategna.backend.messaging.message.service;

import com.muyategna.backend.messaging.message.entity.Message;
import com.muyategna.backend.messaging.message.entity.MessageParticipant;
import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageParticipantService {
    void addCustomerParticipant(MessageThread messageThread, Long customerId);

    void addServiceProviderParticipant(MessageThread savedThread, Long serviceProviderId);

    Page<MessageParticipant> findAllByProviderId(Long serviceProviderId, Pageable pageable);

    Page<MessageParticipant> findAllByCustomerId(Long customerId, Pageable pageable);

    void updateUnreadCountExceptSender(MessageThread thread, Message message);

    List<MessageParticipant> findAllByThreadId(Long id);

//    MessageParticipant getParticipantByThreadIdAndParticipantIdAndParticipantType(Long threadId, Long participantId, ParticipantType participantType);

    void createParticipant(MessageThread thread, Long participantId, ParticipantType participantType);

//    MessageParticipant getParticipantByThreadIdAndParticipantTypeAndUserProfileId(Long threadId, ParticipantType senderType, Long useProfileId);
}
