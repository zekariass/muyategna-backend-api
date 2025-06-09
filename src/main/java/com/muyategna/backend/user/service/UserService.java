package com.muyategna.backend.user.service;

import com.muyategna.backend.user.dto.UserRegistrationDto;
import com.muyategna.backend.user.dto.UserRegistrationResultDto;

import java.util.List;

/**
 * UserService interface for user-related operations.
 * This interface defines methods for user registration, retrieval, and role assignment.
 */
public interface UserService {

    /**
     * Retrieves all users.
     *
     * @return a list of UserRegistrationResultDto containing user information.
     */
    List<UserRegistrationResultDto> getAll();

    /**
     * Retrieves a user by username.
     *
     * @param username the user's username.
     * @return a list of UserRegistrationResultDto containing the user's information.
     */
    List<UserRegistrationResultDto> getByUsername(String username);

    /**
     * Retrieves a user by id.
     *
     * @param id the user's id.
     * @return a UserRegistrationResultDto containing the user's information.
     */
    UserRegistrationResultDto getById(String id);

    /**
     * Assigns a role to a user.
     *
     * @param userId   the user's id.
     * @param roleName the name of the role to assign.
     */
    void assignRole(String userId, String roleName);

    /**
     * Creates a new user.
     *
     * @param userRegistrationDto the user registration request data.
     * @return a UserRegistrationResultDto containing the created user's information.
     */
    UserRegistrationResultDto createUser(UserRegistrationDto userRegistrationDto);


}
