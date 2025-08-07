package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;

import java.util.Optional;

public interface NotificationTemplateTranslationService {
    Optional<NotificationTemplateTranslation> getNotificationTemplateTranslationByNotificationTemplateAndLanguage(Long templateId, Long languageId);
}
