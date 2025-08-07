package com.muyategna.backend.messaging.notification.mapper;

import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import com.muyategna.backend.messaging.notification.entity.Notification;

public final class NotificationMapper {
    public static Notification toEntity(NotificationDto dto) {
        Notification entity = new Notification();
        entity.setStatus(dto.getStatus());
        entity.setNotificationCategory(dto.getNotificationCategory());
        entity.setRecipientEntityType(dto.getRecipientEntityType());
        entity.setRecipientEntityId(dto.getRecipientEntityId());
        entity.setPayload(dto.getPayload());
        entity.setSubject(dto.getSubject());
        entity.setErrorMessage(dto.getErrorMessage());
        entity.setNotificationTemplate(dto.getNotificationTemplate());
        entity.setNotificationChannel(dto.getNotificationChannel());
        entity.setBody(dto.getBody());

        return entity;
    }
}
