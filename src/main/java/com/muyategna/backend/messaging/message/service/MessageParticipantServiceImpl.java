package com.muyategna.backend.messaging.message.service;

import com.muyategna.backend.messaging.message.entity.Message;
import com.muyategna.backend.messaging.message.entity.MessageParticipant;
import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import com.muyategna.backend.messaging.message.repository.MessageParticipantRepository;
import com.muyategna.backend.service_provider.service.ServiceEmployeeService;
import com.muyategna.backend.system.context.AuditorContextHolder;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageParticipantServiceImpl implements MessageParticipantService {

    private final MessageParticipantRepository messageParticipantRepository;
    private final ServiceEmployeeService serviceEmployeeService;

    @Autowired
    public MessageParticipantServiceImpl(MessageParticipantRepository messageParticipantRepository, ServiceEmployeeService serviceEmployeeService) {
        this.messageParticipantRepository = messageParticipantRepository;
        this.serviceEmployeeService = serviceEmployeeService;
    }

    @Override
    public void addCustomerParticipant(MessageThread messageThread, Long customerId) {
        log.info("Adding participant {} to message thread {}", customerId, messageThread.getId());
        MessageParticipant messageParticipant = new MessageParticipant();
        messageParticipant.setThread(messageThread);
        messageParticipant.setParticipantId(customerId);
        messageParticipant.setParticipantType(ParticipantType.CUSTOMER);
        messageParticipantRepository.save(messageParticipant);

        log.info("Participant {} added to message thread {}", customerId, messageThread.getId());
    }

    @Override
    public void addServiceProviderParticipant(MessageThread savedThread, Long serviceProviderId) {
        log.info("Adding service provider participant {} to message thread {}", serviceProviderId, savedThread.getId());

        // Check if the current user is employee of the service provide
        boolean isCurrentUserEmployeeOfServiceProvider = serviceEmployeeService.existByServiceProviderIdAndUserProfileId(serviceProviderId, AuditorContextHolder.getUserProfile().getId());

        if (!isCurrentUserEmployeeOfServiceProvider) {
            throw new BadRequestException("The logged in user is not an employee of the service provider with id " + serviceProviderId);
        }

        MessageParticipant messageParticipant = new MessageParticipant();
        messageParticipant.setThread(savedThread);
        messageParticipant.setParticipantId(serviceProviderId);
        messageParticipant.setParticipantType(ParticipantType.SERVICE_PROVIDER);
        messageParticipantRepository.save(messageParticipant);
        log.info("Service provider participant {} added to message thread {}", serviceProviderId, savedThread.getId());
    }

    @Override
    public Page<MessageParticipant> findAllByProviderId(Long serviceProviderId, Pageable pageable) {
        log.info("Finding all participants for service provider {}", serviceProviderId);
        Page<MessageParticipant> participants = messageParticipantRepository.findDistinctThreadsByParticipantIdAndType(serviceProviderId, ParticipantType.SERVICE_PROVIDER, pageable);
        log.info("Found {} participants for service provider {}", participants.getTotalElements(), serviceProviderId);
        return participants;
    }

    @Override
    public Page<MessageParticipant> findAllByCustomerId(Long customerId, Pageable pageable) {
        log.info("Finding all participants for customer {}", customerId);
        Page<MessageParticipant> participants = messageParticipantRepository.findDistinctThreadsByParticipantIdAndType(customerId, ParticipantType.CUSTOMER, pageable);
        log.info("Found {} participants for customer {}", participants.getTotalElements(), customerId);
        return participants;
    }

    @Override
    public void updateUnreadCountExceptSender(MessageThread thread, Message message) {
        log.info("Updating unread count for thread {}", thread.getId());

        List<MessageParticipant> participants = messageParticipantRepository.findAllByThread_Id(thread.getId());

        List<MessageParticipant> participantsToUpdate = new ArrayList<>();
        for (MessageParticipant participant : participants) {
            if (participant.getUserProfile() != null && participant.getUserProfile().getId().equals(AuditorContextHolder.getUserProfile().getId())) {
                continue;
            }
            participant.setUnreadCount(participant.getUnreadCount() + 1);
            participantsToUpdate.add(participant);
            messageParticipantRepository.save(participant);
        }

        messageParticipantRepository.saveAll(participantsToUpdate);

        log.info("Unread count updated for thread {}", thread.getId());
    }

    @Override
    public List<MessageParticipant> findAllByThreadId(Long id) {
        log.info("Finding all participants for thread {}", id);
        List<MessageParticipant> participants = messageParticipantRepository.findAllByThread_Id(id);
        log.info("Found {} participants for thread {}", participants.size(), id);
        return participants;
    }

//    @Override
//    public MessageParticipant getParticipantByThreadIdAndParticipantIdAndParticipantType(Long threadId, Long participantId, ParticipantType participantType) {
//        log.info("Getting participant by thread id {} and participant id {} and participant type {}", threadId, participantId, participantType);
//        MessageParticipant messageParticipant = messageParticipantRepository.findByThread_IdAndParticipantIdAndParticipantType(threadId, participantId, participantType);
//        log.info("Found participant by thread id {} and participant id {} and participant type {}", threadId, participantId, participantType);
//        return messageParticipant;
//    }

    @Override
    public void createParticipant(MessageThread thread, Long participantId, ParticipantType participantType) {
        log.info("Creating participant for thread {} and sender id {} and sender type {}", thread.getId(), participantId, participantType);
        MessageParticipant messageParticipant = new MessageParticipant();

        messageParticipant.setUserProfile(AuditorContextHolder.getUserProfile());
        messageParticipant.setThread(thread);
        messageParticipant.setParticipantId(participantId);
        messageParticipant.setParticipantType(participantType);
        messageParticipantRepository.save(messageParticipant);
        log.info("Participant created for thread {} and sender id {} and sender type {}", thread.getId(), participantId, participantType);
    }

//    @Override
//    public MessageParticipant getParticipantByThreadIdAndParticipantTypeAndUserProfileId(Long threadId, ParticipantType senderType, Long useProfileId) {
//        log.info("Getting participant by thread id {} and sender type {} and user profile id {}", threadId, senderType, useProfileId);
//        MessageParticipant messageParticipant = messageParticipantRepository.findByThreadIdAndParticipantTypeAndUserProfileId(threadId, senderType, useProfileId);
//        log.info("Found participant by thread id {} and sender type {} and user profile id {}", threadId, senderType, useProfileId);
//        return messageParticipant;
//    }
}
