package com.muyategna.backend.common;

import java.util.List;

/**
 * Constants class containing various constants used throughout the application.
 * <p>
 * This class is not meant to be instantiated and contains static final fields for easy access to commonly used values.
 * </p>
 *
 * @author Zekarias
 */
public final class Constants {

    public static final String LANGUAGE_LOCALE_HEADER = "X-Language-Code";
    public static final String COUNTRY_CODE_HEADER = "X-Country-Code";

    public static final List<String> PUBLIC_API_PATHS_FOR_FILTER = List.of(
            "/api/auth",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resource",
            "/webjars",
            "/docs"
    );

    public static final List<String> PUBLIC_API_ENDPOINTS_FOR_ACCESS = List.of(
            "/",
            "/api/v1/users",
            "/api/v1/users/**",
            "/api/v1/public/common/languages/**",
            "/api/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resource/**",
            "/webjars/**",
            "/docs"
    );

    public static final String CACHE_LOCATION = "location";
    public static final String CACHE_SYSTEM = "system";
    public static final String CACHE_COMMON = "common";
    public static final String CACHE_SERVICE = "service";
    public static final String[] ALL_CACHE_NAMES = {CACHE_LOCATION, CACHE_SYSTEM, CACHE_COMMON, CACHE_SERVICE};


    //===================================================================================

    public static final String DEFAULT_PAGE_SIZE_NAME = "DEFAULT_PAGE_SIZE";

    private Constants() {
        // Prevent instantiation
    }
}
