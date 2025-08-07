package com.muyategna.backend.messaging.notification.strategies.email_strategies;

import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import com.muyategna.backend.messaging.notification.entity.NotificationChannel;
import com.muyategna.backend.messaging.notification.enums.ChannelName;
import com.muyategna.backend.messaging.notification.enums.NotificationType;
import com.muyategna.backend.messaging.notification.service.NotificationChannelStrategy;
import com.muyategna.backend.system.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailProviderResolver implements NotificationChannelStrategy {

    private final BrevoEmailStrategy brevoEmailStrategy;
    private final SendGridEmailStrategy sendGridEmailStrategy;
    private final AmazonSESEmailStrategy amazonSESEmailStrategy;
    private final SystemConfigService systemConfigService;

    @Autowired
    public EmailProviderResolver(BrevoEmailStrategy brevoEmailStrategy, SendGridEmailStrategy sendGridEmailStrategy, AmazonSESEmailStrategy amazonSESEmailStrategy, SystemConfigService systemConfigService) {
        this.brevoEmailStrategy = brevoEmailStrategy;
        this.sendGridEmailStrategy = sendGridEmailStrategy;
        this.amazonSESEmailStrategy = amazonSESEmailStrategy;
        this.systemConfigService = systemConfigService;
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel.getName() == ChannelName.EMAIL;
    }

    @Override
    public void sendNotification(NotificationDto notification) {
        if (this.supports(notification.getNotificationChannel())) {
            String emailServiceProvider = "";

            if (notification.getNotificationType() == NotificationType.TRANSACTIONAL) {
                emailServiceProvider = systemConfigService.getSystemConfigByName("TRANSACTIONAL_EMAIL_SERVICE_PROVIDER").getValue();
            } else if (notification.getNotificationType() == NotificationType.MARKETING) {
                emailServiceProvider = systemConfigService.getSystemConfigByName("MARKETING_EMAIL_SERVICE_PROVIDER").getValue();
            } else if (notification.getNotificationType() == NotificationType.LIFECYCLE) {
                emailServiceProvider = systemConfigService.getSystemConfigByName("LIFECYCLE_EMAIL_SERVICE_PROVIDER").getValue();
            }


            if (emailServiceProvider.equalsIgnoreCase("brevo")) {
                brevoEmailStrategy.sendEmail(notification);
            } else if (emailServiceProvider.equalsIgnoreCase("sendgrid")) {
                sendGridEmailStrategy.sendEmail(notification);
            } else if (emailServiceProvider.equalsIgnoreCase("amazon_ses")) {
                amazonSESEmailStrategy.sendEmail(notification);
            }
        }

    }

}
