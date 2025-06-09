package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.country.CountryTranslationCreateDto;
import com.muyategna.backend.location.dto.country.CountryTranslationDto;
import com.muyategna.backend.location.dto.country.CountryTranslationMinimalDto;
import com.muyategna.backend.location.dto.country.CountryTranslationUpdateDto;
import com.muyategna.backend.location.entity.CountryTranslation;

public final class CountryTranslationMapper {
//    public CountryTranslationDto toDto(CountryTranslation countryTranslation) {
//        if (countryTranslation == null) {
//            return null;
//        }
//        CountryTranslationDto dto = new CountryTranslationDto();
//        dto.setId(countryTranslation.getId());
//        dto.setCountry(countryTranslation.getCountry().getId());
//        dto.setLanguage(countryTranslation.getLanguage().getId());
//        dto.setName(countryTranslation.getName());
//        dto.setContinent(countryTranslation.getContinent());
//        dto.setDescription(countryTranslation.getDescription());
//        dto.setCreatedAt(countryTranslation.getCreatedAt());
//        dto.setUpdatedAt(countryTranslation.getUpdatedAt());
//        return dto;
//    }
//
//    public CountryTranslation toEntity(CountryTranslationDto dto, Country country, Language language) {
//        if (dto == null) {
//            return null;
//        }
//        CountryTranslation countryTranslation = new CountryTranslation();
//        countryTranslation.setCountry(country);
//        countryTranslation.setLanguage(language);
//        countryTranslation.setName(dto.getName());
//        countryTranslation.setContinent(dto.getContinent());
//        countryTranslation.setDescription(dto.getDescription());
//        countryTranslation.setCreatedAt(dto.getCreatedAt());
//        countryTranslation.setUpdatedAt(dto.getUpdatedAt());
//        return countryTranslation;
//    }


    /**
     * Maps a {@link CountryTranslation} to a {@link CountryTranslationMinimalDto}.
     *
     * <p>If the given country translation is null, this method returns null.
     *
     * @param countryTranslation the country translation to map
     * @return the mapped country translation minimal DTO
     */
    public static CountryTranslationMinimalDto toMinimalDto(CountryTranslation countryTranslation) {
        if (countryTranslation == null) {
            return null;
        }
        CountryTranslationMinimalDto dto = new CountryTranslationMinimalDto();
        dto.setId(countryTranslation.getId());
        dto.setCountryId(countryTranslation.getCountry().getId());
        dto.setName(countryTranslation.getName());
        dto.setLanguageId(countryTranslation.getLanguage().getId());
        return dto;
    }


    /**
     * Converts a {@link CountryTranslation} entity to a {@link CountryTranslationDto}.
     *
     * <p>If the given country translation is null, this method returns null.
     *
     * @param countryTranslation the country translation entity to convert
     * @return the converted country translation DTO
     */

    public static CountryTranslationDto toDto(CountryTranslation countryTranslation) {
        if (countryTranslation == null) {
            return null;
        }
        CountryTranslationDto dto = new CountryTranslationDto();
        dto.setId(countryTranslation.getId());
        dto.setCountryId(countryTranslation.getCountry().getId());
        dto.setLanguageId(countryTranslation.getLanguage().getId());
        dto.setName(countryTranslation.getName());
        dto.setContinent(countryTranslation.getContinent());
        dto.setDescription(countryTranslation.getDescription());
        dto.setCreatedAt(countryTranslation.getCreatedAt());
        dto.setUpdatedAt(countryTranslation.getUpdatedAt());
        return dto;
    }


    /**
     * Converts a {@link CountryTranslation} entity to a {@link CountryTranslationCreateDto}.
     *
     * <p>If the given country translation is null, this method returns null.
     *
     * @param countryTranslation the country translation entity to convert
     * @return the converted country translation create DTO
     */
    public static CountryTranslationCreateDto toCreateDto(CountryTranslation countryTranslation) {
        if (countryTranslation == null) {
            return null;
        }
        CountryTranslationCreateDto dto = new CountryTranslationCreateDto();
        dto.setCountryId(countryTranslation.getCountry().getId());
        dto.setLanguageId(countryTranslation.getLanguage().getId());
        dto.setName(countryTranslation.getName());
        dto.setContinent(countryTranslation.getContinent());
        dto.setDescription(countryTranslation.getDescription());
        return dto;
    }


    /**
     * Converts a {@link CountryTranslation} entity to a {@link CountryTranslationUpdateDto}.
     *
     * <p>If the given country translation is null, this method returns null.
     *
     * @param countryTranslation the country translation entity to convert
     * @return the converted country translation update DTO
     */
    public static CountryTranslationUpdateDto toUpdateDto(CountryTranslation countryTranslation) {
        if (countryTranslation == null) {
            return null;
        }
        CountryTranslationUpdateDto dto = new CountryTranslationUpdateDto();
        dto.setId(countryTranslation.getId());
        dto.setCountryId(countryTranslation.getCountry().getId());
        dto.setLanguageId(countryTranslation.getLanguage().getId());
        dto.setName(countryTranslation.getName());
        dto.setContinent(countryTranslation.getContinent());
        dto.setDescription(countryTranslation.getDescription());
        return dto;
    }

}
