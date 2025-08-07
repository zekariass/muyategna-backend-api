package com.muyategna.backend.messaging.notification.repository;

import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

//    @Query(
//            """
//                    SELECT DISTINCT nt
//                    FROM NotificationTemplate nt
//                    LEFT JOIN FETCH nt.translations
//                    WHERE nt.notificationCategory.id = :notificationCategoryId
//                    AND nt.isActive = true
//                    """)
//    List<NotificationTemplate> findByCategoryWithTranslations(@Param("notificationCategoryId") Long notificationCategoryId);

    @Query("""
                SELECT DISTINCT nt
                FROM NotificationTemplate nt
                LEFT JOIN FETCH nt.translations
                WHERE nt.notificationCategory.id = :notificationCategoryId
                AND nt.isActive = true
            """)
    List<NotificationTemplate> findByCategoryWithTranslations(@Param("notificationCategoryId") Long notificationCategoryId);
}
