package com.muyategna.backend.messaging.notification.strategies.email_strategies;

import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import com.muyategna.backend.messaging.notification.exception.NotificationSendingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@Slf4j
public class AmazonSESEmailStrategy implements EmailStrategy {

    private final SesClient sesClient;

    @Autowired
    public AmazonSESEmailStrategy(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    @Override
    public void sendEmail(NotificationDto notificationDto) {
        String to = notificationDto.getTo();
        String subject = notificationDto.getSubject();
        String body = notificationDto.getBody();
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(
                            Destination.builder()
                                    .toAddresses(to)
                                    .build())
                    .message(
                            Message.builder()
                                    .subject(Content.builder().data(subject).build())
                                    .body(Body.builder().html(Content.builder().data(body).build()).build())
                                    .build())
                    .source("zs.negese@gmail.com")
                    .build();
            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("Email sent with message ID: {}", response.messageId());
        } catch (SesException e) {
            throw new NotificationSendingException("Failed to send email", e);
        }
    }
}
