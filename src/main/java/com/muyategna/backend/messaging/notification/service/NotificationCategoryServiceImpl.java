package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.messaging.notification.dto.NotificationCategoryDto;
import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import com.muyategna.backend.messaging.notification.mapper.NotificationCategoryMapper;
import com.muyategna.backend.messaging.notification.repository.NotificationCategoryRepository;
import com.muyategna.backend.redis.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationCategoryServiceImpl implements NotificationCategoryService {

    private final NotificationCategoryRepository notificationCategoryRepository;

    @Autowired
    public NotificationCategoryServiceImpl(NotificationCategoryRepository notificationCategoryRepository) {
        this.notificationCategoryRepository = notificationCategoryRepository;
    }

    @Cacheable(value = CacheConstants.CACHE_NOTIFICATION,
            key = "T(com.muyategna.backend.redis.NotificationCacheUtil).notificationCategoryByNameKey(#categoryName + '_' + #recipientType)",
            unless = "#result == null")
    @Override
    public NotificationCategoryDto getNotificationCategoryByNameAndRecipientType(String categoryName, NotificationRecipientType recipientType) {
        log.info("Retrieving notification category by name: {}", categoryName);
        return NotificationCategoryMapper.toDto(notificationCategoryRepository.findByNameAndRecipientType(categoryName, recipientType)
                .orElse(null));
    }
}
