package com.muyategna.backend.messaging.notification.service.email;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;

import java.util.Map;

public interface EmailRendererService {
    String renderBody(NotificationTemplate template, NotificationTemplateTranslation translation, Map<String, Object> payload);

    String renderSubject(NotificationTemplate template, NotificationTemplateTranslation translation, Map<String, Object> payload);

    String renderBody(NotificationTemplate template, String templateText, Map<String, Object> payload);

    String renderSubject(NotificationTemplate template, String templateText, Map<String, Object> payload);

}
