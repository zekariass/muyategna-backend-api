package com.muyategna.backend.user.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * DTO for user registration response.
 * This class is used to transfer user registration response data between the client and server.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationResultDto {
    private String keycloakUserId;
    private UserProfileDto userProfile;
    private String username;
    private String email;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private List<String> requiredActions;
    private List<String> realmRoles;
    private Map<String, List<String>> clientRoles;
    private Long createdTimestamp;
    private String message;
}
