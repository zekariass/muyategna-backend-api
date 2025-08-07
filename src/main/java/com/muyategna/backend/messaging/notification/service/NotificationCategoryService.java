package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.dto.NotificationCategoryDto;
import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;

public interface NotificationCategoryService {
    NotificationCategoryDto getNotificationCategoryByNameAndRecipientType(String categoryName, NotificationRecipientType recipientType);
}
