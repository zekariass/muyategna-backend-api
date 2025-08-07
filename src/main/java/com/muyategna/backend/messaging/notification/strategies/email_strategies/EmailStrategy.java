package com.muyategna.backend.messaging.notification.strategies.email_strategies;

import com.muyategna.backend.messaging.notification.dto.NotificationDto;

public interface EmailStrategy {
    void sendEmail(NotificationDto notification);
}
