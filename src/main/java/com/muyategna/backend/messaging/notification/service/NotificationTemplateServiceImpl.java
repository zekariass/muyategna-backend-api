package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import com.muyategna.backend.messaging.notification.repository.NotificationTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    public NotificationTemplateServiceImpl(NotificationTemplateRepository notificationTemplateRepository) {
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Override
    public List<NotificationTemplate> getNotificationTemplatesWithTranslationsByNotificationCategory(Long notificationCategoryId) {
        log.info("Retrieving Notification Templates by notification category ID: {}", notificationCategoryId);
        return notificationTemplateRepository.findByCategoryWithTranslations(notificationCategoryId);
    }
}
