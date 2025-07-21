package com.muyategna.backend.user.service;

import com.muyategna.backend.user.dto.UserProfileDto;
import com.muyategna.backend.user.entity.UserProfile;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public interface UserProfileService {

    UserProfileDto findByKeycloakUserId(UUID id);
    UserProfileDto save(UserProfile userProfile);
    UserProfileDto createProfileFromKeycloakAuthToken(JwtAuthenticationToken authToken);
    void updateDefaultLanguageForUser(Long id, String languageCode);

}
