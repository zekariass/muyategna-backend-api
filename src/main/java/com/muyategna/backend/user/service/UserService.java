package com.muyategna.backend.user.service;

import com.muyategna.backend.user.dto.UserRegistrationDto;
import com.muyategna.backend.user.dto.UserRegistrationResultDto;
import jakarta.ws.rs.core.Response;

/**
 * UserService interface for user-related operations.
 * This interface defines methods for user registration, retrieval, and role assignment.
 */
public interface UserService {

//    List<UserRegistrationResultDto> getAll();

//    List<UserRegistrationResultDto> getByUsername(String username);

//    UserRegistrationResultDto getById(String id);

    void assignRole(String userId, String roleName);

    UserRegistrationResultDto createUser(UserRegistrationDto userRegistrationDto);

    Response deleteUser(String keycloakUserId);

    boolean isCurrentUserAdmin();
}
