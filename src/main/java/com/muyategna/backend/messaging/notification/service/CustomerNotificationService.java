package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.dto.queue.*;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface CustomerNotificationService {


    void notifyCustomerAboutNewJobRequest(NewJobRequestNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

    void notifyCustomerAboutNewClaim(NewClaimNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

    void notifyCustomerAboutJobStatusChange(JobStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

    void notifyCustomerAboutRequoteRequest(RequoteRequestNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

    void notifyCustomerAboutNewQuote(NewQuoteNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

    void notifyCustomerAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

    void notifyCustomerAboutClaimStatusChange(ClaimStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

}