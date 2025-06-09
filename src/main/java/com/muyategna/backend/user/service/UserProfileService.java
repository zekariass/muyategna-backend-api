package com.muyategna.backend.user.service;

import com.muyategna.backend.user.dto.UserProfileDto;
import com.muyategna.backend.user.entity.UserProfile;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public interface UserProfileService {

    public UserProfileDto findByKeycloakUserId(UUID id);
    public UserProfileDto save(UserProfile userProfile);
    public UserProfileDto createProfileFromKeycloakAuthToken(JwtAuthenticationToken authToken);
    public void updateDefaultLanguageForUser(Long id, String languageCode);

}
