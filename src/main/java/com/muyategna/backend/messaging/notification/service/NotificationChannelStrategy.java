package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import com.muyategna.backend.messaging.notification.entity.NotificationChannel;

public interface NotificationChannelStrategy {
    boolean supports(NotificationChannel channel);

    void sendNotification(NotificationDto notification);
}
