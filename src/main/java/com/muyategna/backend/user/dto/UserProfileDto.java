package com.muyategna.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private UUID keycloakUserId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String profilePictureUrl;
    private LocalDateTime lastLogin;
    private String referralCode;
    private long addressId;
    private String fullAddress;
    private Long defaultLanguageId;
}
