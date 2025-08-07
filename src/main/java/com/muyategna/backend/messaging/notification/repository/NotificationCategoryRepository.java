package com.muyategna.backend.messaging.notification.repository;

import com.muyategna.backend.messaging.notification.entity.NotificationCategory;
import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationCategoryRepository extends JpaRepository<NotificationCategory, Long> {
    Optional<NotificationCategory> findByNameAndRecipientType(String categoryName, NotificationRecipientType recipientType);
}
