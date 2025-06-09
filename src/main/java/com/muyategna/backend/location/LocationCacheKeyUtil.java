package com.muyategna.backend.location;

public class LocationCacheKeyUtil {
    public static String regionsByCountryAndLanguageKey(Long countryId, Long languageId) {
        return String.format("regions_for_country_%d_language_%d", countryId, languageId);
    }

    public static String regionByIdAndLanguageKey(Long regionId, Long languageId) {
        return String.format("region_%d_language_%d", regionId, languageId);
    }

    public static String countryByIdAndLanguageKey(Long countryId, Long languageId) {
        return String.format("country_%d_language_%d", countryId, languageId);
    }

    public static String countryByIsoCode2Key(String isoCode2) {
        return String.format("country_by_iso_code2_%s", isoCode2);
    }

    public static String countryByIdKey(Long countryId) {
        return String.format("country_by_id_%d", countryId);
    }


    public static String countriesByLanguageKey(Long languageId) {
        return String.format("countries_by_language_%d", languageId);
    }
}