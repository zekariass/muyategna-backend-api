package com.muyategna.backend.user.mapper;

import com.muyategna.backend.user.dto.UserProfileDto;
import com.muyategna.backend.user.entity.UserProfile;

public final class UserProfileMapper {

    public static UserProfileDto toDto(UserProfile userProfile) {
        if (userProfile == null) return null;
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(userProfile.getId());
        userProfileDto.setKeycloakUserId(userProfile.getKeycloakUserId());
        userProfileDto.setFirstName(userProfile.getFirstName());
        userProfileDto.setMiddleName(userProfile.getMiddleName());
        userProfileDto.setLastName(userProfile.getLastName());
        userProfileDto.setPhoneNumber(userProfile.getPhoneNumber());
        userProfileDto.setEmail(userProfile.getEmail());
        userProfileDto.setProfilePictureUrl(userProfile.getProfilePictureUrl());
        userProfileDto.setLastLogin(userProfile.getLastLogin());
        userProfileDto.setReferralCode(userProfile.getReferralCode());
        if (userProfile.getAddress() != null) {
            userProfileDto.setAddressId(userProfile.getAddress().getId());
            userProfileDto.setFullAddress(userProfile.getAddress().getFullAddress());
        }
        userProfileDto.setDefaultLanguageId(userProfile.getDefaultLanguage().getId());
        return userProfileDto;
    }


    public static UserProfile toEntity(UserProfileDto userProfileDto) {
        if (userProfileDto == null) return null;
        UserProfile userProfile = new UserProfile();
        userProfile.setId(userProfileDto.getId());
        userProfile.setKeycloakUserId(userProfileDto.getKeycloakUserId());
        userProfile.setFirstName(userProfileDto.getFirstName());
        userProfile.setMiddleName(userProfileDto.getMiddleName());
        userProfile.setLastName(userProfileDto.getLastName());
        userProfile.setPhoneNumber(userProfileDto.getPhoneNumber());
        userProfile.setEmail(userProfileDto.getEmail());
        userProfile.setProfilePictureUrl(userProfileDto.getProfilePictureUrl());
        userProfile.setLastLogin(userProfileDto.getLastLogin());
        userProfile.setReferralCode(userProfileDto.getReferralCode());
        return userProfile;
    }
}
