package com.muyategna.backend.messaging.notification.service;

//
//@Component
//@Slf4j
//public class NotificationDispatcherService {
//    private final List<NotificationChannelStrategy> notificationChannelStrategies;
//
//    @Autowired
//    public NotificationDispatcherService(List<NotificationChannelStrategy> notificationChannelStrategies) {
//        this.notificationChannelStrategies = notificationChannelStrategies;
//    }
//
//    public void sendNotification(NotificationDto notification) {
//        notificationChannelStrategies.forEach(notificationChannelStrategy -> {
//            if (notificationChannelStrategy.supports(notification.getNotificationChannel())) {
//                log.info("Sending notification to channel {}", notification.getNotificationChannel());
//                notificationChannelStrategy.sendNotification(notification);
//            }
//        });
//    }
//}


import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NotificationDispatcherService {

    private final List<NotificationChannelStrategy> notificationChannelStrategies;

    @Autowired
    public NotificationDispatcherService(List<NotificationChannelStrategy> notificationChannelStrategies) {
        this.notificationChannelStrategies = notificationChannelStrategies;
    }

    public void sendNotification(NotificationDto notification) {
        boolean handled = false;

        for (NotificationChannelStrategy strategy : notificationChannelStrategies) {
            if (strategy.supports(notification.getNotificationChannel())) {
//                try {
                log.info("Dispatching notification via strategy: {}", strategy.getClass().getSimpleName());
                strategy.sendNotification(notification);
                handled = true;
//                } catch (NotificationSendingException e) {
//                    log.error("Failed to send notification via {}: {}", strategy.getClass().getSimpleName(), e.getMessage(), e);
//                    throw new NotificationSendingException("Failed to send notification via " + strategy.getClass().getSimpleName() + ": " + e.getMessage(), e);
//                }
            }
        }

        if (!handled) {
            log.warn("No strategy found for notification channel: {}", notification.getNotificationChannel());
        }
    }
}