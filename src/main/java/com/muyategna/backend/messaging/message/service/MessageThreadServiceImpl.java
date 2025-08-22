package com.muyategna.backend.messaging.message.service;

import com.muyategna.backend.messaging.message.dto.MessageThreadCreateDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadDetailViewDto;
import com.muyategna.backend.messaging.message.dto.MessageThreadListViewDto;
import com.muyategna.backend.messaging.message.entity.MessageParticipant;
import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import com.muyategna.backend.messaging.message.mapper.MessageThreadMapper;
import com.muyategna.backend.messaging.message.repository.MessageThreadRepository;
import com.muyategna.backend.messaging.message.repository.projection.MessageThreadProjection;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MessageThreadServiceImpl implements MessageThreadService {

    private final MessageThreadRepository messageThreadRepository;
    private final MessageParticipantService messageParticipantService;
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public MessageThreadServiceImpl(MessageThreadRepository messageThreadRepository, MessageParticipantService messageParticipantService, ServiceProviderRepository serviceProviderRepository, UserProfileRepository userProfileRepository) {
        this.messageThreadRepository = messageThreadRepository;
        this.messageParticipantService = messageParticipantService;
        this.serviceProviderRepository = serviceProviderRepository;
        this.userProfileRepository = userProfileRepository;
    }


    /**
     * This service is responsible for managing message threads.
     * <p>
     * Message threads are used to group messages that are related to each other.
     *
     * @author nguyentv2
     * @since 2020-11-29
     */
    @Override
    public MessageThreadListViewDto createMessageThread(MessageThreadCreateDto messageThreadCreateDto) {
        log.info("Creating message thread: {}", messageThreadCreateDto);

        if (messageThreadCreateDto == null) {
            log.error("MessageThreadCreateDto is null");
            throw new BadRequestException("MessageThreadCreateDto cannot be null");
        }
        // Check if service provider exists
        ServiceProvider serviceProvider = serviceProviderRepository.findById(messageThreadCreateDto.getServiceProviderId()).orElseThrow(() -> new ResourceNotFoundException("Service provider not found with id " + messageThreadCreateDto.getServiceProviderId()));

        // Check if Customer exists
        UserProfile customer = userProfileRepository.findById(messageThreadCreateDto.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + messageThreadCreateDto.getCustomerId()));

        MessageThread messageThread = new MessageThread();
        messageThread.setSubject(messageThreadCreateDto.getSubject());
        messageThread.setContextType(messageThreadCreateDto.getContextType());
        messageThread.setContextId(messageThreadCreateDto.getContextId());
        messageThread.setServiceProvider(serviceProvider);
        messageThread.setCustomer(customer);

        MessageThread savedThread = messageThreadRepository.save(messageThread);
        log.info("Message thread created: {}", savedThread);

        // Add the customer as a participant to the thread
        messageParticipantService.addCustomerParticipant(savedThread, messageThreadCreateDto.getCustomerId());
        messageParticipantService.addServiceProviderParticipant(savedThread, messageThreadCreateDto.getServiceProviderId());

        List<MessageParticipant> messageParticipants = messageParticipantService.findAllByThreadId(savedThread.getId());

        savedThread.setParticipants(messageParticipants);
        return MessageThreadMapper.toListViewDto(savedThread);
    }


    /**
     * Get a message thread by id
     *
     * @param messageThreadId message thread id
     * @return message thread
     */
    @Override
    public MessageThreadDetailViewDto getMessageThreadDetailById(Long messageThreadId) {
        MessageThread messageThread = messageThreadRepository.findByIdWithParticipants(messageThreadId).orElseThrow(() -> new ResourceNotFoundException("Message thread not found"));
        return MessageThreadMapper.toDetailViewDto(messageThread);
    }


    /**
     * Get all message threads for a service provider
     *
     * @param serviceProviderId service provider id
     * @param pageable          pagination object
     * @return list of message threads
     */
    @Override
    public Page<MessageThreadListViewDto> getAllMessageThreadsForServiceProvider(Long serviceProviderId, Pageable pageable) {
        Page<MessageThread> threads = messageThreadRepository.findAllByServiceProviderId(serviceProviderId, pageable);

        return threads.map(MessageThreadMapper::toListViewDto);

    }


    /**
     * Get all message threads for a customer
     *
     * @param customerId customer id
     * @param pageable   pagination object
     * @return list of message threads
     */
    @Override
    public Page<MessageThreadListViewDto> getAllMessageThreadsForCustomer(Long customerId, Pageable pageable) {

        if (customerId == null) {
            log.error("Customer ID is null");
            throw new BadRequestException("Customer ID cannot be null");
        }

        if (!Objects.equals(AuditorContextHolder.getUserProfile().getId(), customerId)) {
            log.error("Customer ID does not match logged in user");
            throw new AccessDeniedException("Customer ID does not match logged in user");
        }

        Page<MessageThread> threads = messageThreadRepository.findAllByCustomerId(customerId, pageable);

        return threads.map(MessageThreadMapper::toListViewDto);
    }


    /**
     * Get distinct threads by participant
     *
     * @param participantId   participant id
     * @param participantType participant type
     * @param pageable        pagination object
     * @return paged list of distinct message threads
     */
    @Override
    public Page<MessageThreadProjection> findDistinctThreadsByParticipant(Long participantId, ParticipantType participantType, Pageable pageable) {
        log.info("Finding distinct threads by participant: {}", participantId);
        return messageThreadRepository.findDistinctThreadsByParticipant(participantId, participantType, pageable);
    }

}
