package com.muyategna.backend.messaging.notification.dto;

import com.muyategna.backend.messaging.notification.entity.NotificationCategory;
import com.muyategna.backend.messaging.notification.entity.NotificationChannel;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import com.muyategna.backend.messaging.notification.enums.NotificationStatus;
import com.muyategna.backend.messaging.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private NotificationRecipientType recipientEntityType;
    private Long recipientEntityId;
    private NotificationTemplate notificationTemplate;
    private NotificationChannel notificationChannel;
    private NotificationCategory notificationCategory;
    private NotificationStatus status;
    private Map<String, Object> payload;
    private String to;
    private String subject;
    private String body;
    private NotificationType notificationType;
    private LocalDateTime sentAt;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
