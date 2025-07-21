package com.muyategna.backend.system.context;

import com.muyategna.backend.user.entity.UserProfile;

public class AuditorContextHolder {
    private static final ThreadLocal<UserProfile> currentUser = new ThreadLocal<>();

    public static void setUserProfile(UserProfile user) {
        currentUser.set(user);
    }

    public static UserProfile getUserProfile() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}

