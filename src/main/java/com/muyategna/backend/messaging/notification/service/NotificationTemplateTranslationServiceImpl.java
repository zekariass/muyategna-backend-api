package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;
import com.muyategna.backend.messaging.notification.repository.NotificationTemplateTranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class NotificationTemplateTranslationServiceImpl implements NotificationTemplateTranslationService {
    private final NotificationTemplateTranslationRepository notificationTemplateTranslationRepository;

    @Autowired
    public NotificationTemplateTranslationServiceImpl(NotificationTemplateTranslationRepository notificationTemplateTranslationRepository) {
        this.notificationTemplateTranslationRepository = notificationTemplateTranslationRepository;
    }

    @Override
    public Optional<NotificationTemplateTranslation> getNotificationTemplateTranslationByNotificationTemplateAndLanguage(Long templateId, Long languageId) {
        log.info("getNotificationTemplateTranslationByNotificationTemplateAndLanguage: templateId: {}, languageId: {}", templateId, languageId);
        return notificationTemplateTranslationRepository.findByTemplateIdAndLanguageId(templateId, languageId);
    }
}
