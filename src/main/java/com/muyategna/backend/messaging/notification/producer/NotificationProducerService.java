package com.muyategna.backend.messaging.notification.producer;

import com.muyategna.backend.messaging.notification.dto.queue.*;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface NotificationProducerService {

    // ========================= SERVICE PROVIDER =========================
    void enqueueNotifyNearByServiceProvidersAboutNewLead(NewLeadNotificationQueueSPMessage queueMessage);

    void enqueueNotifyServiceProviderAboutNewClaim(NewClaimNotificationQueueSPMessage notificationMessage);

    void enqueueNotifyServiceProviderAboutJobStatusChange(JobStatusChangeNotificationQueueSPMessage notificationMessage);

    void enqueueNotifyServiceProviderAboutRequoteRequest(RequoteRequestNotificationQueueSPMessage notificationMessage);

    void enqueueNotifyServiceProviderAboutNewQuote(NewQuoteNotificationQueueSPMessage notificationMessage);

    void enqueueNotifyServiceProviderAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueSPMessage notificationMessage);

    void enqueueNotifyServiceProviderAboutClaimStatusChange(ClaimStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel);


    // ========================= CUSTOMER =========================

    void enqueueNotifyCustomerAboutNewJobRequest(NewJobRequestNotificationQueueCustomerMessage queueMessage);

    void enqueueNotifyCustomerAboutNewClaim(NewClaimNotificationQueueCustomerMessage queueMessage);

    void enqueueNotifyCustomerAboutJobStatusChange(JobStatusChangeNotificationQueueCustomerMessage queueMessage);

    void enqueueNotifyCustomerAboutRequoteRequest(RequoteRequestNotificationQueueCustomerMessage queueMessage);

    void enqueueNotifyCustomerAboutNewQuote(NewQuoteNotificationQueueCustomerMessage queueMessage);

    void enqueueNotifyCustomerAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueCustomerMessage queueMessage);

    void enqueueNotifyCustomerAboutClaimStatusChange(ClaimStatusChangeNotificationQueueCustomerMessage notificationMessage, Message message, Channel channel);

}
