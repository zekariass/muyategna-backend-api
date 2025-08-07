package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.dto.queue.*;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface ServiceProviderNotificationService {
    void notifyNearByServiceProvidersAboutNewLead(NewLeadNotificationQueueSPMessage notificationMessage, Message message, Channel channel);

    void notifyServiceProviderAboutNewClaim(NewClaimNotificationQueueSPMessage notificationMessage, Message message, Channel channel);

    void notifyServiceProviderAboutJobStatusChange(JobStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel);

    void notifyServiceProviderAboutRequoteRequest(RequoteRequestNotificationQueueSPMessage notificationMessage, Message message, Channel channel);

    void notifyServiceProviderAboutNewQuote(NewQuoteNotificationQueueSPMessage notificationMessage, Message message, Channel channel);

    void notifyServiceProviderAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel);

    void notifyServiceProviderAboutClaimStatusChange(ClaimStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel);
    

//    NotificationDto buildNotificationObject(ServiceProvider provider, NotificationChannel channelType, NotificationTemplate template, NotificationCategoryDto categoryDto, Map<String, Object> payload, NotificationTemplateTranslation translation);
}
