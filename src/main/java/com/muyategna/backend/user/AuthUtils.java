package com.muyategna.backend.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public class AuthUtils {
    public static UUID getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && (authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return UUID.fromString(jwtAuth.getToken().getSubject());
        }
        return null;
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && (authentication instanceof JwtAuthenticationToken);
    }
}
