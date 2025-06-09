package com.muyategna.backend.user.service;

import com.muyategna.backend.system.config.KeycloakPropertiesConfig;
import com.muyategna.backend.user.dto.UserRegistrationDto;
import com.muyategna.backend.user.dto.UserRegistrationResultDto;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.exception.DuplicateUserException;
import com.muyategna.backend.user.exception.RoleDoesNotFoundException;
import com.muyategna.backend.user.exception.UserCreationException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link UserService} which uses Keycloak REST API to interact with users.
 * This implementation assumes that the Keycloak REST API is available and configured properly.
 *
 * @author Zekarias
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserProfileService userProfileService;

    /**
     * Keycloak client used to interact with Keycloak REST API.
     */
    private final Keycloak keycloak;

    /**
     * Configuration properties for Keycloak.
     */
    private final KeycloakPropertiesConfig propertiesConfig;

    /**
     * Constructor for UserServiceImpl.
     *
     * @param keycloak           the Keycloak client used to interact with Keycloak REST API
     * @param propertiesConfig   the configuration properties for Keycloak
     * @param userProfileService the service for user profile
     */
    @Autowired
    public UserServiceImpl(UserProfileService userProfileService, Keycloak keycloak, KeycloakPropertiesConfig propertiesConfig) {
        this.userProfileService = userProfileService;
        this.keycloak = keycloak;
        this.propertiesConfig = propertiesConfig;
    }

    /**
     * Retrieves the UsersResource instance for interacting with users in Keycloak.
     *
     * @return the UsersResource instance
     */
    private UsersResource usersResourceInstance() {
        return keycloak.realm(propertiesConfig.getRealm()).users();
    }


    /**
     * Maps a UserRepresentation object to a UserRegistrationResultDto object.
     *
     * @param userRepresentation the UserRepresentation object to map
     * @return the mapped UserRegistrationResultDto object
     */
    private UserRegistrationResultDto mapToUserRegistrationResponseDto(UserRepresentation userRepresentation) {
        return UserRegistrationResultDto.builder()
                .userId(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .enabled(userRepresentation.isEnabled())
                .emailVerified(userRepresentation.isEmailVerified())
                .createdTimestamp(userRepresentation.getCreatedTimestamp())
                .requiredActions(userRepresentation.getRequiredActions())
                .clientRoles(userRepresentation.getClientRoles())
                .realmRoles(userRepresentation.getRealmRoles())
                .message("User created successfully")
                .build();
    }

    /**
     * Retrieves all users from Keycloak.
     *
     * @return a list of UserRegistrationResultDto objects representing all users
     */
    @Override
    public List<UserRegistrationResultDto> getAll() {
        return usersResourceInstance().list().stream().map(this::mapToUserRegistrationResponseDto).toList();
    }

    /**
     * Retrieves users by username from Keycloak.
     *
     * @param username the username to search for
     * @return a list of UserRegistrationResultDto objects representing the users with the specified username
     */
    @Override
    public List<UserRegistrationResultDto> getByUsername(String username) {
        return usersResourceInstance()
                .search(username).stream().map(this::mapToUserRegistrationResponseDto).toList();
    }

    /**
     * Retrieves a user by id from Keycloak.
     *
     * @param id the id of the user to retrieve
     * @return a UserRegistrationResultDto object representing the user with the specified id
     */
    @Override
    public UserRegistrationResultDto getById(String id) {
        var user = usersResourceInstance()
                .get(id)
                .toRepresentation();

        return mapToUserRegistrationResponseDto(user);
    }

    /**
     * Assigns a role to a user in Keycloak.
     *
     * @param userId   the id of the user to assign the role to
     * @param roleName the name of the role to assign
     */
    @Override
    public void assignRole(String userId, String roleName) {

        RoleRepresentation roleRepresentation;
        try {
            roleRepresentation = keycloak.realm(propertiesConfig.getRealm()).roles().get(roleName).toRepresentation();
        } catch (ProcessingException e) {
            log.error("Error retrieving role {}: {}", roleName, e.getMessage());
            throw new RoleDoesNotFoundException("Error retrieving role");
        }
        usersResourceInstance()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRepresentation));
    }

    /**
     * Creates a new user in Keycloak.
     *
     * @param userRegistrationDto the DTO containing user registration data
     * @return a UserRegistrationResultDto object representing the created user to return to the frontend client
     * @throws DuplicateUserException if a user with the same username or email already exists
     * @throws UserCreationException  if there is an error during user creation
     */
    @Override
    public UserRegistrationResultDto createUser(UserRegistrationDto userRegistrationDto) {

        UserRepresentation userRepresentation = buildUserRepresentation(userRegistrationDto);
        try (Response response = usersResourceInstance().create(userRepresentation)) {

            int statusCode = response.getStatus();
            switch (statusCode) {
                case 201 -> {
                    String location = response.getHeaderString("Location");
                    UUID userId = UUID.fromString(location.substring(location.lastIndexOf('/') + 1));
                    UserProfile userProfile = buildUserProfileObject(userRegistrationDto, userId);
                    userProfileService.save(userProfile);

                    log.info("User {} successfully created in Keycloak", userRegistrationDto.getUsername());
                    return mapToUserRegistrationResponseDto(userRepresentation);
                }
                case 409 -> {
                    log.error("Duplicate user {}", userRegistrationDto.getUsername());
                    throw new DuplicateUserException(MessageFormat.format("Duplicate user {0}", userRegistrationDto.getUsername()));
                }
                default -> {
                    log.error("Error creating user: status code {}", statusCode);
                    throw new UserCreationException(MessageFormat.format("Error from keycloak in creating user {0}", userRegistrationDto.getUsername()));
                }
            }
        } catch (ProcessingException e) {
            log.error("Error creating user in Keycloak", e);
            throw new UserCreationException(e.getMessage());
        }

    }


    /**
     * Builds a UserRepresentation object from a UserRegistrationDto object.
     *
     * @param userRegistrationDto the DTO containing user registration data
     * @return a UserRepresentation object representing the user
     */
    private UserRepresentation buildUserRepresentation(UserRegistrationDto userRegistrationDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userRegistrationDto.getUsername());
        userRepresentation.setCredentials(Collections.singletonList(buildCredentialRepresentation(userRegistrationDto.getPassword())));
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(userRegistrationDto.getEmail());
        userRepresentation.setFirstName(userRegistrationDto.getFirstName());
        userRepresentation.setLastName(userRegistrationDto.getLastName());
        userRepresentation.setEmailVerified(true);

        return userRepresentation;
    }

    /**
     * Builds a CredentialRepresentation object from a password string.
     *
     * @param password the password string
     * @return a CredentialRepresentation object representing the password
     */
    private CredentialRepresentation buildCredentialRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }

    /**
     * Builds a UserProfile object from a UserRegistrationDto object and a Keycloak user ID.
     *
     * @param userRegistrationDto the DTO containing user registration data
     * @param keycloakUserId      the Keycloak user ID
     * @return a UserProfile object representing the user profile
     */
    private UserProfile buildUserProfileObject(UserRegistrationDto userRegistrationDto, UUID keycloakUserId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setKeycloakUserId(keycloakUserId);
        userProfile.setEmail(userRegistrationDto.getEmail());
        userProfile.setFirstName(userRegistrationDto.getFirstName());
        userProfile.setMiddleName(userRegistrationDto.getMiddleName());
        userProfile.setLastName(userRegistrationDto.getLastName());
        userProfile.setProfilePictureUrl(userRegistrationDto.getProfilePictureUrl());
        userProfile.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        userProfile.setReferralCode(userRegistrationDto.getReferralCode());
        userProfile.setIsStaff(userRegistrationDto.getIsStaff());
        userProfile.setAddress(userRegistrationDto.getAddress());

        return userProfile;
    }


}
