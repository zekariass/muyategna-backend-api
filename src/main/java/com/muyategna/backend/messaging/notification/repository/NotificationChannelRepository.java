package com.muyategna.backend.messaging.notification.repository;

import com.muyategna.backend.messaging.notification.entity.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Long> {
    List<NotificationChannel> findAllByIsActive(boolean isActive);
}
