package com.muyategna.backend.system.config;

import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorAware")
@Slf4j
public class UserProfileAuditorAware implements AuditorAware<Long> {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileAuditorAware(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Returns the current auditor, which is the UserProfile associated with the authenticated user.
     * If no user is authenticated or if the user does not have a UserProfile, an empty Optional is returned.
     *
     * @return An Optional containing the UserProfile of the current auditor, or empty if not available.
     */
    @NotNull
    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwt) {
                String keycloakUserId = jwt.getSubject();

                UUID uuid;
                try {
                    uuid = UUID.fromString(keycloakUserId);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid UUID format: {}", keycloakUserId);
                    return Optional.empty();
                }

                Optional<UserProfile> userProfile = userProfileRepository.findByKeycloakUserId(uuid);
                if (userProfile.isPresent()) {
                    return Optional.of(userProfile.get().getId());
                } else {
                    log.warn("No UserProfile found for Keycloak ID: {}", keycloakUserId);
                }
            }
        } catch (Exception e) {
            log.warn("Error in getCurrentAuditor: {}", e.getMessage());
        }

        return Optional.empty();
    }

}