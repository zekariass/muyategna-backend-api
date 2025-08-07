package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.messaging.notification.dto.queue.*;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class CustomerNotificationServiceImpl implements CustomerNotificationService {

    @RabbitListener(queues = Constants.NOTIFY_CUSTOMER_ABOUT_NEW_JOB_REQUEST_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyCustomerAboutNewJobRequest(NewJobRequestNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {

    }

    @RabbitListener(queues = Constants.NOTIFY_CUSTOMER_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyCustomerAboutNewClaim(NewClaimNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {

    }

    @RabbitListener(queues = Constants.NOTIFY_CUSTOMER_ABOUT_JOB_STATUS_CHANGE_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyCustomerAboutJobStatusChange(JobStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {

    }

    @RabbitListener(queues = Constants.NOTIFY_CUSTOMER_ABOUT_REQUOTE_REQUEST_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyCustomerAboutRequoteRequest(RequoteRequestNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {

    }

    @RabbitListener(queues = Constants.NOTIFY_CUSTOMER_ABOUT_NEW_QUOTE_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyCustomerAboutNewQuote(NewQuoteNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {

    }

    @RabbitListener(queues = Constants.NOTIFY_CUSTOMER_ABOUT_QUOTE_STATUS_CHANGE_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyCustomerAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {

    }

    @Override
    public void notifyCustomerAboutClaimStatusChange(ClaimStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel) {
        
    }
}
