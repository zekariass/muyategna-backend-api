package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.entity.NotificationChannel;

import java.util.List;

public interface NotificationChannelService {
    List<NotificationChannel> getNotificationChannelsByIsActive(boolean isActive);
}
