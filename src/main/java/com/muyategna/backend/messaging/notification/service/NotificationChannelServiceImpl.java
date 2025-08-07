package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.entity.NotificationChannel;
import com.muyategna.backend.messaging.notification.repository.NotificationChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationChannelServiceImpl implements NotificationChannelService {

    private final NotificationChannelRepository notificationChannelRepository;

    @Autowired
    public NotificationChannelServiceImpl(NotificationChannelRepository notificationChannelRepository) {
        this.notificationChannelRepository = notificationChannelRepository;
    }

    @Override
    public List<NotificationChannel> getNotificationChannelsByIsActive(boolean isActive) {
        log.info("Getting notification channels by isActive: {}", isActive);
        return notificationChannelRepository.findAllByIsActive(isActive);
    }
}
