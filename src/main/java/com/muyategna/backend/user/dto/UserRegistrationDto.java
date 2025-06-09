package com.muyategna.backend.user.dto;

import com.muyategna.backend.location.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for user registration request.
 * This class is used to transfer user registration data between the client and server.
 */
@Data
@Builder
public class UserRegistrationDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;

    private String profilePictureUrl;

    private String referralCode;

    private Boolean isStaff = false;

    private Address address;
//    private List<String> roles; // List of role names
}
