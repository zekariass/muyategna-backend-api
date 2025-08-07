package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;

import java.util.List;

public interface NotificationTemplateService {
    List<NotificationTemplate> getNotificationTemplatesWithTranslationsByNotificationCategory(Long notificationCategoryId);
}
