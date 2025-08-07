package com.muyategna.backend.messaging.notification.strategies.email_strategies;

import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class BrevoEmailStrategy implements EmailStrategy {

    @Override
    public void sendEmail(NotificationDto notificationDto) {

    }
}
