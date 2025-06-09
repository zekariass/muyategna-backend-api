package com.muyategna.backend.common;

public class CommonCacheKeyUtil {

    public static String defaultLanguageForCountryKey(Long countryId) {
        return String.format("default_language_for_country_%d", countryId);
    }

    public static String languagesByCountryKey(Long countryId) {
        return String.format("languages_by_country_%d", countryId);
    }

    public static String languageByLocaleKey(String code) {
        return String.format("language_by_locale_%s", code);
    }

    public static String allLanguagesKey() {
        return "all_languages";
    }

    public static String languageByIdKey(Long languageId) {
        return String.format("language_by_id_%d", languageId);
    }
}
