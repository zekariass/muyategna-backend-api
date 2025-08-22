package com.muyategna.backend.messaging.message.service;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.messaging.message.dto.MessageAttachmentUploadQueueMessage;
import com.muyategna.backend.messaging.message.dto.MessageCreateDto;
import com.muyategna.backend.messaging.message.dto.MessageListViewDto;
import com.muyategna.backend.messaging.message.entity.Message;
import com.muyategna.backend.messaging.message.entity.MessageParticipant;
import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import com.muyategna.backend.messaging.message.mapper.MessageMapper;
import com.muyategna.backend.messaging.message.repository.MessageRepository;
import com.muyategna.backend.messaging.message.repository.MessageThreadRepository;
import com.muyategna.backend.messaging.message.service.producer.MessageQueueProducerService;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.storage.config.cloudflare.CloudflareR2Properties;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.repository.UserProfileRepository;
import com.rabbitmq.client.Channel;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserProfileRepository userProfileRepository;
    private final MessageThreadRepository messageThreadRepository;
    private final MessageQueueProducerService messageQueueProducerService;
    private final MessageParticipantService messageParticipantService;

    private final S3Client s3Client;
    private final CloudflareR2Properties r2Properties;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              ServiceProviderRepository serviceProviderRepository,
                              UserProfileRepository userProfileRepository,
                              MessageThreadRepository messageThreadRepository,
                              MessageQueueProducerService messageQueueProducerService, MessageParticipantService messageParticipantService,
                              S3Client s3Client,
                              CloudflareR2Properties r2Properties) {
        this.messageRepository = messageRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.userProfileRepository = userProfileRepository;
        this.messageThreadRepository = messageThreadRepository;
        this.messageQueueProducerService = messageQueueProducerService;
        this.messageParticipantService = messageParticipantService;
        this.s3Client = s3Client;
        this.r2Properties = r2Properties;
    }


    /**
     * Create a new message
     *
     * @param messageDto message dto
     * @return message view dto
     */
    @Transactional
    @Override
    public MessageListViewDto createMessage(MessageCreateDto messageDto) {
        log.info("Creating new message for thread {} from sender {} ({})",
                messageDto.getThreadId(), messageDto.getSenderId(), messageDto.getSenderType());

        // Null-safe attachments
        List<MultipartFile> files = Optional.ofNullable(messageDto.getAttachments())
                .orElse(Collections.emptyList());

        // Load thread and participants
        MessageThread thread = messageThreadRepository.findByIdWithParticipants(messageDto.getThreadId())
                .orElseThrow(() -> new ResourceNotFoundException("Thread not found with id " + messageDto.getThreadId()));
        List<MessageParticipant> participants = thread.getParticipants();

        // Validate sender is part of the thread
        validateSenderInThread(messageDto.getSenderId(), messageDto.getSenderType(), participants);

        // Create and populate message entity
        Message message = new Message();
        message.setSenderId(getSenderEntityId(messageDto.getSenderId(), messageDto.getSenderType()));
        message.setSenderType(messageDto.getSenderType());
        message.setThread(thread);
        message.setContent(messageDto.getContent());

        if (messageDto.getReplyTo() != null) {
            message.setReplyTo(messageRepository.findById(messageDto.getReplyTo()).orElse(null));
        }

        // Save message
        Message savedMessage = messageRepository.save(message);
        log.info("Message saved with ID: {}", savedMessage.getId());

        // Ensure participant exists
        ensureParticipantExists(thread, messageDto.getSenderId(), messageDto.getSenderType(), participants);

        // Update unread counts
        messageParticipantService.updateUnreadCountExceptSender(thread, savedMessage);

        // Enqueue attachment upload
        enqueueAttachments(savedMessage, files);

        // TODO: Send notification to other participants

        // Map to DTO
        return MessageMapper.toListViewDto(savedMessage);
    }

    /**
     * Validate sender is part of the thread
     *
     * @param senderId     sender ID
     * @param senderType   sender type
     * @param participants thread participants
     */
    private void validateSenderInThread(Long senderId, ParticipantType senderType, List<MessageParticipant> participants) {
        boolean senderIsValid = participants.stream()
                .anyMatch(p -> p.getParticipantId().equals(senderId) && p.getParticipantType().equals(senderType));
        if (!senderIsValid) {
            throw new BadRequestException("Sender is not a valid participant for the current thread");
        }
    }

    /**
     * Get sender entity ID
     *
     * @param senderId   sender ID
     * @param senderType sender type
     * @return sender entity ID
     */
    private Long getSenderEntityId(Long senderId, ParticipantType senderType) {
        return switch (senderType) {
            case CUSTOMER -> userProfileRepository.findById(senderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sender not found"))
                    .getId();
            case SERVICE_PROVIDER -> serviceProviderRepository.findById(senderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sender not found"))
                    .getId();
        };
    }


    /**
     * Ensure participant exists
     *
     * @param thread       thread
     * @param senderId     sender ID
     * @param senderType   sender type
     * @param participants thread participants
     */
    private void ensureParticipantExists(MessageThread thread, Long senderId, ParticipantType senderType, List<MessageParticipant> participants) {
        boolean exists = participants.stream()
                .anyMatch(p -> p.getUserProfile() != null
                        && p.getUserProfile().getId().equals(AuditorContextHolder.getUserProfile().getId()));
        if (!exists) {
            log.info("Participant not found for sender {} ({})", senderId, senderType);
            log.info("Creating new participant for thread {} from sender {} ({})", thread.getId(), senderId, senderType);
            messageParticipantService.createParticipant(thread, senderId, senderType);
        }
    }

    /**
     * Enqueue attachments for upload
     *
     * @param message message
     * @param files   files
     */
    private void enqueueAttachments(Message message, List<MultipartFile> files) {
        if (files.isEmpty()) return;
        try {
            messageQueueProducerService.enqueueMessageAttachmentUpload(message, files);
            log.info("Message attachments enqueued for upload");
        } catch (Exception e) {
            log.error("Error in enqueuing message attachments for upload", e);
        }
    }


    /**
     * Get paged messages by thread ID
     *
     * @param threadId thread ID
     * @param pageable pageable
     * @return paged messages
     */
    @Override
    public Page<MessageListViewDto> getPagedMessagesByThreadId(Long threadId, Pageable pageable) {
        log.info("Retrieving messages for thread ID: {}", threadId);
        Page<Message> messages = messageRepository.findByThread_Id(threadId, pageable);
        log.info("Messages retrieved successfully for thread ID: {}", threadId);
        return messages.map(MessageMapper::toListViewDto);
    }


    /**
     * Upload message attachment
     *
     * @param queueMessage queue message
     * @param message      message
     * @param channel      channel
     * @throws Exception exception
     */
    @RabbitListener(queues = Constants.MESSAGE_ATTACHMENT_UPLOAD_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public void uploadAttachment(MessageAttachmentUploadQueueMessage queueMessage,
                                 org.springframework.amqp.core.Message message,
                                 Channel channel) throws Exception {
        log.info("Received message attachment upload for message ID: {}", queueMessage.getMessageId());
        boolean messageAckedOrNacked = false;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String bucket = r2Properties.getBucketName();
            String basePath = "message_attachments/" + queueMessage.getMessageId() + "/";

            String newFilename = queueMessage.getNewFilename();
            String ext = queueMessage.getExtension();
            String contentType = queueMessage.getContentType();
            byte[] content = queueMessage.getAttachment();

            String folder = "";
            String normalizedExt = ext.toLowerCase();
            if (Constants.IMAGE_EXTENSIONS.contains(normalizedExt)) {
                folder = "images/";
            } else if (Constants.VIDEO_EXTENSIONS.contains(normalizedExt)) {
                folder = "videos/";
            } else if (Constants.DOCUMENT_EXTENSIONS.contains(normalizedExt)) {
                folder = "documents/";
            } else if (Constants.AUDIO_EXTENSIONS.contains(normalizedExt)) {
                folder = "audio/";
            } else {
                log.warn("Unsupported file extension: {}", ext);
                throw new IllegalArgumentException("Unsupported file extension: " + ext);
            }

            String key = basePath + folder + newFilename + "." + ext;

            // Upload to S3 (R2)
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .build();

            log.info("Uploading attachment to R2: {}", key);
            s3Client.putObject(request, RequestBody.fromBytes(content));
            log.info("Attachment uploaded to R2: {}", key);

            // Save to DB
            Message messageFromDb = messageRepository.findById(queueMessage.getMessageId()).orElseThrow(() -> new ResourceNotFoundException("Message not found for updating attachment"));

            List<String> updated = new ArrayList<>(messageFromDb.getAttachments());
            updated.add(key);
            messageFromDb.setAttachments(updated); // Replace the list entirely

            messageRepository.save(messageFromDb);

            log.info("Message is updated for attachment: {}", key);

            // ACK if everything succeeds
            channel.basicAck(deliveryTag, false);
            messageAckedOrNacked = true;

        } catch (Exception e) {
            log.error("Error handling message attachment file upload: {}", e.getMessage(), e);

            // NACK only if retry interceptor is exhausted
            // Let retry interceptor do its work; we only nack if it's final failure
            channel.basicReject(deliveryTag, false); // false = don't requeue
            messageAckedOrNacked = true;
            throw e;
        } finally {
            // Safety net
            if (!messageAckedOrNacked) {
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException ioException) {
                    log.error("Failed to reject message in finally block", ioException);
                }
            }
        }
    }
}
