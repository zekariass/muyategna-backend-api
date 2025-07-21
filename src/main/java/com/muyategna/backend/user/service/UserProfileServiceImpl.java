package com.muyategna.backend.user.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.mapper.LanguageMapper;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.user.dto.UserProfileDto;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.mapper.UserProfileMapper;
import com.muyategna.backend.user.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final LanguageService languageService;

    @Autowired
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, LanguageService languageService) {
        this.userProfileRepository = userProfileRepository;
        this.languageService = languageService;
    }

    @Cacheable(value = CacheConstants.CACHE_USER,
            key = "T(com.muyategna.backend.redis.UserCacheKeyUtil).getUserByIdKey(#id)",
            unless = "#result == null")
    @Override
    public UserProfileDto findByKeycloakUserId(UUID id) {
        UserProfile userProfile = userProfileRepository.findByKeycloakUserId(id).orElse(null);
        return UserProfileMapper.toDto(userProfile);
    }

    @Override
    public UserProfileDto save(UserProfile userProfile) {
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return UserProfileMapper.toDto(savedUserProfile);
    }

    @Override
    public UserProfileDto createProfileFromKeycloakAuthToken(JwtAuthenticationToken authToken) {
        UserProfile userProfile = buildUserProfileObjectFromAuthToken(authToken);
        return UserProfileMapper.toDto(userProfileRepository.save(userProfile));
    }

    private UserProfile buildUserProfileObjectFromAuthToken(JwtAuthenticationToken authToken) {
        UserProfile userProfile = new UserProfile();
        userProfile.setKeycloakUserId(UUID.fromString(authToken.getToken().getSubject()));
        userProfile.setEmail(authToken.getToken().getClaimAsString("email"));
        userProfile.setFirstName(authToken.getToken().getClaimAsString("given_name"));
        userProfile.setLastName(authToken.getToken().getClaimAsString("family_name"));

        userProfile.setCountry(CountryMapper.toEntity(CountryContextHolder.getCountry()));

        LanguageDto defaultLanguage = languageService.getDefaultLanguageForCountry(CountryContextHolder.getCountry()).orElseThrow(() -> new RuntimeException("Platform default language not found."));

        userProfile.setDefaultLanguage(LanguageMapper.toEntity(defaultLanguage, CountryContextHolder.getCountry()));
        return userProfile;
    }

    @Override
    public void updateDefaultLanguageForUser(Long id, String languageLocal) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        if (userProfile.isPresent()) {
            LanguageDto languageDto = languageService.getLanguageByLocale(languageLocal);
            if (languageDto != null) {
                userProfile.get().setDefaultLanguage(LanguageMapper.toEntity(languageDto, CountryContextHolder.getCountry()));
                userProfileRepository.save(userProfile.get());
            }
        }
    }
}
