package com.muyategna.backend.system.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.InvalidLanguageCodeException;
import com.muyategna.backend.system.exception.UserDoesNotHaveDefaultLanguageException;
import com.muyategna.backend.user.dto.UserProfileDto;
import com.muyategna.backend.user.exception.UserProfileDoesNotExistException;
import com.muyategna.backend.user.service.UserProfileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
@Slf4j
public class LanguageFilter extends OncePerRequestFilter {

    private final UserProfileService userProfileService;
    private final LanguageService languageService;

    @Autowired
    public LanguageFilter(UserProfileService userProfileService, LanguageService languageService) {
        this.userProfileService = userProfileService;
        this.languageService = languageService;
    }

    /**
     * Sets the language context for the request. The language is determined by checking the 'X-Language-Code' header in the request.
     * If the header is not present, the user's default language is used. If the user does not have a default language, the default language
     * for the country that the request is coming from is used.
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet exception occurs
     * @throws IOException      if an IO exception occurs
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            LanguageDto resolvedLanguage = null;

            //Get language code from the request header
            String languageLocale = request.getHeader(Constants.LANGUAGE_LOCALE_HEADER).strip();

            // Authentication object to check if the user is authenticated
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // Get the country from CountryContextHolder
            CountryDto countryDto = CountryContextHolder.getCountry();

            List<LanguageDto> languages = new ArrayList<>(languageService.getAllLanguagesByCountryPublic(countryDto));
            LanguageDto globalLanguage = languageService.getGlobalLanguage();

            // The user must be using country specific languages or the global language
            languages.add(globalLanguage);

            // Check if the language code sent as language header is valid
            boolean noneMatch = languages.stream().noneMatch(lang -> lang.getLocale().equals(languageLocale));
            if (!languageLocale.isEmpty() && noneMatch) {
                throw new InvalidLanguageCodeException("Invalid language code: " + languageLocale + " for country: " + countryDto.getName());
            }

            // If the user is authenticated use the user's default language
            if (auth != null && auth.isAuthenticated() && (auth instanceof JwtAuthenticationToken jwtAuth)) {
                UUID userId = UUID.fromString(jwtAuth.getToken().getSubject());
                UserProfileDto userProfileDto = userProfileService.findByKeycloakUserId(userId);

                // If the user profile is not found, create a new one from the keycloak auth token
                if (userProfileDto == null) {
                    userProfileDto = userProfileService.createProfileFromKeycloakAuthToken(jwtAuth);
                }

                if (userProfileDto == null) {
                    throw new UserProfileDoesNotExistException("Something went wrong when creating user profile.");
                }

                // Get the use's default language
                Long languageId = userProfileDto.getDefaultLanguageId();
                resolvedLanguage = languageService.getLanguageById(languageId);

                // Update the default language of the user if the language header code
                // is different from the user's default language saved in user profile
                if (!languageLocale.isEmpty() && !(resolvedLanguage.getLocale().equals(languageLocale))) {
                    userProfileService.updateDefaultLanguageForUser(userProfileDto.getId(), languageLocale);
                    resolvedLanguage = languageService.getLanguageByLocale(languageLocale);
                }
            }


            // If user is not logged in
            // get language by language code from db or
            // use the default language for the country retrieved from the db where isDefault is true.
            if (resolvedLanguage == null) {
                if (!languageLocale.isEmpty()) {
                    resolvedLanguage = languageService.getLanguageByLocale(languageLocale);
                } else {
                    resolvedLanguage = languageService.getDefaultLanguageForCountry(countryDto)
                            .orElse(null);
                }
            }

            if (resolvedLanguage == null) {
                throw new UserDoesNotHaveDefaultLanguageException("User does not have a default language set.");
            }

            log.info("Resolved language of request is: {}", resolvedLanguage.getName());

            LanguageContextHolder.setLanguage(resolvedLanguage);
            LocaleContextHolder.setLocale(Locale.forLanguageTag(resolvedLanguage.getLocale()));

            log.info("Language {} and locale {} is set for request: {}", resolvedLanguage.getLocale(), resolvedLanguage.getLocale(), request.getRequestURI());
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            log.error("Error setting language context for request: {}", request.getRequestURI(), e);

            // Format the response if the response is not yet committed
            if (!response.isCommitted()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                ApiResponse<Object> apiResponse = ApiResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .error(e.getClass().getSimpleName())
                        .success(false)
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .build();

                try {
                    String json = new ObjectMapper().writeValueAsString(apiResponse);
                    response.getWriter().write(json);
                    response.getWriter().flush(); // Force the response to be sent immediately
                } catch (IOException ioException) {
                    log.error("Failed to write error response", ioException);
                }
            } else {
                log.warn("Response already committed, could not write error body.");
            }
        } finally {
            // Clear and reset the context holders after the request
            LanguageContextHolder.clear(); // Clear the LanguageContextHolder to avoid memory leaks
            LocaleContextHolder.resetLocaleContext(); // Reset the LocaleContextHolder to avoid memory leaks
        }

    }


    /**
     * Checks if the request path starts with any of the public api paths. If it does
     * the filter should not be applied.
     *
     * @param request the incoming request
     * @return true if the request path starts with any of the public api paths, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return Constants.PUBLIC_API_PATHS_FOR_FILTER.stream().anyMatch(path::startsWith);
    }
}
