package com.muyategna.backend.messaging.notification.repository;

import com.muyategna.backend.messaging.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
