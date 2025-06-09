package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.country.*;
import com.muyategna.backend.location.entity.Country;

public final class CountryMapper {

    public static CountryLocalizedDto toLocalizedDto(CountryDto countryDto, CountryTranslationDto translationDto) {
        if (countryDto == null || translationDto == null) {
            return null;
        }
        CountryLocalizedDto dto = new CountryLocalizedDto();
        dto.setId(translationDto.getId());
        dto.setName(translationDto.getName());
        dto.setDescription(translationDto.getDescription());
        dto.setContinent(translationDto.getContinent());
        dto.setCountryIsoCode2(countryDto.getCountryIsoCode2());
        dto.setCountryIsoCode3(countryDto.getCountryIsoCode3());
        dto.setCountryIsoCodeNumeric(countryDto.getCountryIsoCodeNumeric());
        return dto;
    }

    /**
     * Maps a {@link Country} to a {@link CountryMinimalDto}.
     *
     * <p>If the given country is null, this method returns null.
     *
     * @param country the country to map
     * @return the mapped country minimal DTO
     */
    public static CountryMinimalDto toMinimalDto(Country country) {
        if (country == null) return null;
        CountryMinimalDto dto = new CountryMinimalDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    /**
     * Converts a {@link Country} entity to a {@link CountryCreateDto}.
     *
     * <p>If the given country is null, this method returns null.
     *
     * @param country the country entity to convert
     * @return the converted country create DTO
     */
    public static CountryCreateDto toCreateDto(Country country) {
        if (country == null) return null;
        CountryCreateDto dto = new CountryCreateDto();
        dto.setName(country.getName());
        dto.setCountryIsoCode2(country.getCountryIsoCode2());
        dto.setCountryIsoCode3(country.getCountryIsoCode3());
        dto.setCountryIsoCodeNumeric(country.getCountryIsoCodeNumeric());
        dto.setContinent(country.getContinent());
        dto.setDescription(country.getDescription());
        return dto;
    }

    /**
     * Converts a {@link Country} entity to a {@link CountryUpdateDto}.
     *
     * <p>If the given country is null, this method returns null.
     *
     * @param country the country entity to convert
     * @return the converted country update DTO
     */
    public static CountryUpdateDto toUpdateDto(Country country) {
        if (country == null) return null;
        CountryUpdateDto dto = new CountryUpdateDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCountryIsoCode2(country.getCountryIsoCode2());
        dto.setCountryIsoCode3(country.getCountryIsoCode3());
        dto.setCountryIsoCodeNumeric(country.getCountryIsoCodeNumeric());
        dto.setContinent(country.getContinent());
        dto.setDescription(country.getDescription());
        return dto;
    }

    /**
     * Converts a {@link Country} entity to a {@link CountryDto}.
     *
     * <p>If the given country is null, this method returns null.
     *
     * @param country the country entity to convert
     * @return the converted country DTO
     */
    public static CountryDto toDto(Country country) {
        if (country == null) {
            return null;
        }
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setDescription(country.getDescription());
        dto.setContinent(country.getContinent());
        dto.setCountryIsoCode2(country.getCountryIsoCode2());
        dto.setCountryIsoCode3(country.getCountryIsoCode3());
        dto.setCountryIsoCodeNumeric(country.getCountryIsoCodeNumeric());
        dto.setCreatedAt(country.getCreatedAt());
        dto.setUpdatedAt(country.getUpdatedAt());
        return dto;
    }

    /**
     * Converts a {@link CountryDto} to a {@link Country} entity.
     *
     * <p>If the given country DTO is null, this method returns null.
     *
     * @param countryDto the country DTO to convert
     * @return the converted country entity
     */
    public static Country toEntity(CountryDto countryDto) {
        if (countryDto == null) {
            return null;
        }
        Country country = new Country();
        country.setId(countryDto.getId());
        country.setName(countryDto.getName());
        country.setDescription(countryDto.getDescription());
        country.setContinent(countryDto.getContinent());
        country.setCountryIsoCode2(countryDto.getCountryIsoCode2());
        country.setCountryIsoCode3(countryDto.getCountryIsoCode3());
        country.setCountryIsoCodeNumeric(countryDto.getCountryIsoCodeNumeric());
        country.setCreatedAt(countryDto.getCreatedAt());
        country.setUpdatedAt(countryDto.getUpdatedAt());
        return country;
    }

    /**
     * Converts a {@link CountryCreateDto} to a {@link Country} entity.
     *
     * <p>If the given country create DTO is null, this method returns null.
     *
     * @param countryCreateDto the country create DTO to convert
     * @return the converted country entity
     */
    public static Country toEntity(CountryCreateDto countryCreateDto) {
        if (countryCreateDto == null) {
            return null;
        }
        Country country = new Country();
        country.setName(countryCreateDto.getName());
        country.setDescription(countryCreateDto.getDescription());
        country.setContinent(countryCreateDto.getContinent());
        country.setCountryIsoCode2(countryCreateDto.getCountryIsoCode2());
        country.setCountryIsoCode3(countryCreateDto.getCountryIsoCode3());
        country.setCountryIsoCodeNumeric(countryCreateDto.getCountryIsoCodeNumeric());
        country.setGlobal(countryCreateDto.isGlobal());
        return country;
    }

    /**
     * Converts a {@link CountryUpdateDto} to a {@link Country} entity.
     *
     * <p>If the given country update DTO is null, this method returns null.
     *
     * @param countryUpdateDto the country update DTO to convert
     * @return the converted country entity
     */
    public static Country toEntity(CountryUpdateDto countryUpdateDto) {
        if (countryUpdateDto == null) {
            return null;
        }
        Country country = new Country();
        country.setId(countryUpdateDto.getId());
        country.setName(countryUpdateDto.getName());
        country.setDescription(countryUpdateDto.getDescription());
        country.setContinent(countryUpdateDto.getContinent());
        country.setCountryIsoCode2(countryUpdateDto.getCountryIsoCode2());
        country.setCountryIsoCode3(countryUpdateDto.getCountryIsoCode3());
        country.setCountryIsoCodeNumeric(countryUpdateDto.getCountryIsoCodeNumeric());
        return country;
    }

    /**
     * Converts a {@link CountryMinimalDto} to a {@link Country} entity.
     *
     * <p>If the given country minimal DTO is null, this method returns null.
     *
     * @param countryMinimalDto the country minimal DTO to convert
     * @return the converted country entity
     */
    public static Country toEntity(CountryMinimalDto countryMinimalDto) {
        if (countryMinimalDto == null) {
            return null;
        }
        Country country = new Country();
        country.setId(countryMinimalDto.getId());
        country.setName(countryMinimalDto.getName());
        return country;
    }
}
