package com.muyategna.backend.messaging.notification.repository;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateTranslationRepository extends JpaRepository<NotificationTemplateTranslation, Long> {
    Optional<NotificationTemplateTranslation> findByTemplateIdAndLanguageId(Long templateId, Long languageId);
}
