package com.muyategna.backend.user.controller;

import com.muyategna.backend.user.dto.UserRegistrationDto;
import com.muyategna.backend.user.dto.UserRegistrationResultDto;
import com.muyategna.backend.user.exception.DuplicateUserException;
import com.muyategna.backend.user.exception.RoleDoesNotFoundException;
import com.muyategna.backend.user.exception.UserCreationException;
import com.muyategna.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserPublicController handles user-related operations such as creating users, assigning roles, and retrieving user information.
 */
@RestController
@RequestMapping("/api/v1/public/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserPublicController {

    private final UserService userService;

    @Autowired
    public UserPublicController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user by id.
     *
     * @param id the user's id.
     * @return a UserRegistrationResultDto containing the user's information.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Retrieve a user by id")
    public ResponseEntity<UserRegistrationResultDto> findById(@Parameter(required = true, description = "User id") @PathVariable String id) {
        var user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user.
     *
     * @param userRegistrationDto the DTO containing user registration data.
     * @return a UserRegistrationResultDto containing the created user's information.
     * @throws DuplicateUserException if a user with the same username or email already exists.
     * @throws UserCreationException  if there is an error during user creation.
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user")
    public ResponseEntity<UserRegistrationResultDto> createUser(@Parameter(required = true, description = "User registration data") @RequestBody UserRegistrationDto userRegistrationDto) {
        UserRegistrationResultDto responseDto = userService.createUser(userRegistrationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId   the id of the user.
     * @param roleName the name of the role to be assigned.
     * @return a response with HTTP status 201 Created if the operation is successful.
     * @throws RoleDoesNotFoundException if the role does not exist.
     */
    @PutMapping("/{userId}/assign/role/{roleName}")
    @Operation(summary = "Assign a role to a user", description = "Assign a role to a user")
    @Parameters({@Parameter(name = "userId", description = "User id"),
            @Parameter(name = "roleName", description = "Role name")})
    public ResponseEntity<?> assignRoleToUser(@PathVariable String userId, @PathVariable String roleName) {

        userService.assignRole(userId, roleName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
