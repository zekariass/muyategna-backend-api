package com.muyategna.backend.messaging.notification.mapper;

import com.muyategna.backend.messaging.notification.dto.NotificationCategoryDto;
import com.muyategna.backend.messaging.notification.entity.NotificationCategory;

public final class NotificationCategoryMapper {
    public static NotificationCategoryDto toDto(NotificationCategory notificationCategory) {
        if (notificationCategory == null) {
            return null;
        }
        return NotificationCategoryDto.builder()
                .id(notificationCategory.getId())
                .name(notificationCategory.getName())
                .recipientType(notificationCategory.getRecipientType())
                .userCanOptOut(notificationCategory.getUserCanOptOut())
                .createdAt(notificationCategory.getCreatedAt())
                .updatedAt(notificationCategory.getUpdatedAt())
                .build();
    }

    public static NotificationCategory toEntity(NotificationCategoryDto notificationCategory) {

        NotificationCategory entity = new NotificationCategory();
        entity.setId(notificationCategory.getId());
        entity.setName(notificationCategory.getName());
        entity.setRecipientType(notificationCategory.getRecipientType());
        entity.setUserCanOptOut(notificationCategory.getUserCanOptOut());
        entity.setCreatedAt(notificationCategory.getCreatedAt());
        entity.setUpdatedAt(notificationCategory.getUpdatedAt());
        return entity;
    }
}
