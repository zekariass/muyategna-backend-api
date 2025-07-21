package com.muyategna.backend.redis;

import java.util.UUID;

public class UserCacheKeyUtil {
    public static String getUserByIdKey(UUID userId) {
        String userIdString = userId.toString().replaceAll("-", "_");
        return String.format("user_by_id_%s", userIdString);
    }
}
