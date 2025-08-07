package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.dto.NotificationCategoryDto;
import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import com.muyategna.backend.messaging.notification.entity.NotificationChannel;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;
import com.muyategna.backend.messaging.notification.entity.ServiceProviderNotificationPreference;
import com.muyategna.backend.messaging.notification.enums.ChannelName;
import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import com.muyategna.backend.messaging.notification.enums.NotificationStatus;
import com.muyategna.backend.messaging.notification.enums.NotificationType;
import com.muyategna.backend.messaging.notification.mapper.NotificationCategoryMapper;
import com.muyategna.backend.messaging.notification.service.email.EmailRendererService;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ServiceProviderNotificationUtils {

    private final EmailRendererService emailRendererService;

    @Autowired
    public ServiceProviderNotificationUtils(EmailRendererService emailRendererService) {
        this.emailRendererService = emailRendererService;
    }


    public ServiceProviderStatistics updateStatisticsForSentNotification(ServiceProviderStatistics serviceProviderStatistics) {
        if (serviceProviderStatistics == null) {
            return null;
        }

        serviceProviderStatistics.setTotalNewJobNotifs(serviceProviderStatistics.getTotalNewJobNotifs() + 1);
        serviceProviderStatistics.setDailyNewJobNotifs(serviceProviderStatistics.getDailyNewJobNotifs() + 1);
        serviceProviderStatistics.setWeeklyNewJobNotifs(serviceProviderStatistics.getWeeklyNewJobNotifs() + 1);
        return serviceProviderStatistics;
    }

    public List<NotificationChannel> getEnabledNotificationChannels(ServiceProvider provider) {
        return provider.getNotificationPreferences().stream()
                .filter(ServiceProviderNotificationPreference::getIsEnabled)
                .map(ServiceProviderNotificationPreference::getNotificationChannel)
                .filter(NotificationChannel::getIsActive)
                .distinct().toList();
    }

    public NotificationDto buildNotificationObject(ServiceProvider provider, NotificationChannel channelType, NotificationTemplate template, NotificationCategoryDto categoryDto, Map<String, Object> payload, NotificationTemplateTranslation translation) {
        NotificationDto notification = new NotificationDto();
        notification.setRecipientEntityType(NotificationRecipientType.SERVICE_PROVIDER);
        notification.setRecipientEntityId(provider.getId());
        notification.setNotificationTemplate(template);
        notification.setNotificationChannel(channelType);
        notification.setNotificationCategory(NotificationCategoryMapper.toEntity(categoryDto));
        notification.setPayload(payload);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setNotificationType(NotificationType.TRANSACTIONAL);

        if (channelType.getName() == ChannelName.EMAIL) {
            String subject = emailRendererService.renderSubject(template, translation, payload);
            String body = emailRendererService.renderBody(template, translation, payload);
            notification.setSubject(subject);
            notification.setBody(body);
            notification.setTo(provider.getBusinessEmail());
        }
        return notification;
    }
}
