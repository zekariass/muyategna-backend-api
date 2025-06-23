package com.muyategna.backend.system.converter;

import com.muyategna.backend.service_provider.service.ServiceEmployeeRoleService;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;

/* * CustomRoleConverter is a Spring component that converts JWT tokens into a collection of GrantedAuthority.
 * It combines roles from Keycloak with roles from the database for a specific user profile.
 */

@Component
public class CustomRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final KeycloakRoleConverter keycloakRoleConverter = new KeycloakRoleConverter();
    private final ServiceEmployeeRoleService serviceEmployeeRoleService;
    private final UserProfileRepository userProfileRepository;

    public CustomRoleConverter(ServiceEmployeeRoleService serviceEmployeeRoleService, UserProfileRepository userProfileRepository) {
        this.serviceEmployeeRoleService = serviceEmployeeRoleService;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Collection<GrantedAuthority> convert(@NotNull Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>(Objects.requireNonNull(keycloakRoleConverter.convert(jwt), "KeycloakRoleConverter returned null for JWT token"));

        UUID keycloakUserId = UUID.fromString(jwt.getSubject()); // Or jwt.getClaim("email") or "preferred_username"

        UserProfile userProfile = userProfileRepository.findByKeycloakUserId(keycloakUserId).orElse(null);

        if (userProfile == null) {
            return authorities;
        }

        // Fetch roles from the database for the user profile
        List<String> dbRoles = serviceEmployeeRoleService.getRolesForEmployeeByUserProfileId(userProfile.getId());

        // If no roles are found in the database, return only Keycloak roles
        dbRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .forEach(authorities::add);

        return authorities;
    }
}

