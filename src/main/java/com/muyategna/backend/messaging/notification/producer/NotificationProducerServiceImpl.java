package com.muyategna.backend.messaging.notification.producer;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.messaging.notification.dto.queue.*;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducerServiceImpl implements NotificationProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NotificationProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    // ===================== SERVICE PROVIDER NOTIFICATION PRODUCERS ===================================================
    @Override
    public void enqueueNotifyNearByServiceProvidersAboutNewLead(NewLeadNotificationQueueSPMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_NEAR_BY_SERVICE_PROVIDERS_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyServiceProviderAboutNewClaim(NewClaimNotificationQueueSPMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_SP_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyServiceProviderAboutJobStatusChange(JobStatusChangeNotificationQueueSPMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_SP_ABOUT_JOB_STATUS_CHANGE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyServiceProviderAboutRequoteRequest(RequoteRequestNotificationQueueSPMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_SP_ABOUT_REQUOTE_REQUEST_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyServiceProviderAboutNewQuote(NewQuoteNotificationQueueSPMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_SP_ABOUT_NEW_QUOTE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyServiceProviderAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueSPMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_SP_ABOUT_QUOTE_STATUS_CHANGE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyServiceProviderAboutClaimStatusChange(ClaimStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_SP_ABOUT_CLAIM_STATUS_CHANGE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }


    // ===================== CUSTOMER NOTIFICATION PRODUCERS ===========================================================
    @Override
    public void enqueueNotifyCustomerAboutNewJobRequest(NewJobRequestNotificationQueueCustomerMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_NEW_JOB_REQUEST_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);

    }

    @Override
    public void enqueueNotifyCustomerAboutNewClaim(NewClaimNotificationQueueCustomerMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);

    }

    @Override
    public void enqueueNotifyCustomerAboutJobStatusChange(JobStatusChangeNotificationQueueCustomerMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_JOB_STATUS_CHANGE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyCustomerAboutRequoteRequest(RequoteRequestNotificationQueueCustomerMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_REQUOTE_REQUEST_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyCustomerAboutNewQuote(NewQuoteNotificationQueueCustomerMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_NEW_QUOTE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyCustomerAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueCustomerMessage notificationMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_QUOTE_STATUS_CHANGE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }

    @Override
    public void enqueueNotifyCustomerAboutClaimStatusChange(ClaimStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.NOTIFY_CUSTOMER_ABOUT_CLAIM_STATUS_CHANGE_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                notificationMessage);
    }
}
