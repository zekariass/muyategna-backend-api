package com.muyategna.backend.redis;

public class NotificationCacheUtil {

    public static String notificationCategoryByNameKey(String name) {
        return String.format("notification_category_by_name_%s", name);
    }
}
