package com.muyategna.backend.common.mapper;

import com.muyategna.backend.common.dto.language.LanguageCreateDto;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.dto.language.LanguageMinimalDto;
import com.muyategna.backend.common.dto.language.LanguageUpdateDto;
import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.mapper.CountryMapper;

public final class LanguageMapper {

    public static LanguageMinimalDto toMinimalDto(Language language) {
        if (language == null) return null;
        LanguageMinimalDto dto = new LanguageMinimalDto();
        dto.setId(language.getId());
        dto.setName(language.getName());
        return dto;
    }

    public static LanguageDto toDto(Language language) {
        if (language == null) return null;
        LanguageDto dto = new LanguageDto();
        dto.setId(language.getId());
        dto.setName(language.getName());
        dto.setCountryId(language.getCountry().getId());
        dto.setLocale(language.getLocale());
        dto.setActive(language.isActive());
        dto.setDefault(language.isDefault());
        dto.setNativeName(language.getNativeName());
        dto.setDirection(language.getDirection());
        dto.setFlagEmoji(language.getFlagEmoji());
        dto.setCreatedAt(language.getCreatedAt());
        dto.setUpdatedAt(language.getUpdatedAt());
        dto.setGlobal(language.isGlobal());
        return dto;
    }

    public static LanguageCreateDto toCreationDto(Language language) {
        if (language == null) return null;
        LanguageCreateDto dto = new LanguageCreateDto();
        dto.setName(language.getName());
        dto.setLocale(language.getLocale());
        dto.setActive(language.isActive());
        dto.setDefault(language.isDefault());
        dto.setNativeName(language.getNativeName());
        dto.setDirection(language.getDirection());
        dto.setFlagEmoji(language.getFlagEmoji());
        dto.setCountryId(language.getCountry().getId());
        return dto;
    }

    public static LanguageUpdateDto toUpdateDto(Language language) {
        if (language == null) return null;
        LanguageUpdateDto dto = new LanguageUpdateDto();
        dto.setId(language.getId());
        dto.setName(language.getName());
        dto.setLocale(language.getLocale());
        dto.setActive(language.isActive());
        dto.setDefault(language.isDefault());
        dto.setCountryId(language.getCountry().getId());
        dto.setNativeName(language.getNativeName());
        dto.setDirection(language.getDirection());
        dto.setFlagEmoji(language.getFlagEmoji());
        return dto;
    }

    public static Language toEntity(LanguageUpdateDto dto, CountryDto countryDto) {
        if (dto == null) return null;
        Language language = new Language();
        language.setId(dto.getId());
        language.setName(dto.getName());
        language.setCountry(CountryMapper.toEntity(countryDto));
        language.setLocale(dto.getLocale());
        language.setActive(dto.isActive());
        language.setDefault(dto.isDefault());
        language.setNativeName(dto.getNativeName());
        language.setDirection(dto.getDirection());
        language.setFlagEmoji(dto.getFlagEmoji());
        return language;
    }

    public static Language toEntity(LanguageCreateDto dto, CountryDto countryDto) {
        if (dto == null) return null;
        Language language = new Language();
        language.setName(dto.getName());
        language.setCountry(CountryMapper.toEntity(countryDto));
        language.setLocale(dto.getLocale());
        language.setActive(dto.isActive());
        language.setDefault(dto.isDefault());
        language.setNativeName(dto.getNativeName());
        language.setDirection(dto.getDirection());
        language.setFlagEmoji(dto.getFlagEmoji());
        return language;
    }

    public static Language toEntity(LanguageDto dto, CountryDto countryDto) {
        if (dto == null) return null;
        Language language = new Language();
        language.setId(dto.getId());
        language.setName(dto.getName());
        language.setCountry(CountryMapper.toEntity(countryDto));
        language.setLocale(dto.getLocale());
        language.setActive(dto.isActive());
        language.setDefault(dto.isDefault());
        language.setNativeName(dto.getNativeName());
        language.setDirection(dto.getDirection());
        language.setFlagEmoji(dto.getFlagEmoji());
        return language;
    }

    public static Language toEntity(LanguageMinimalDto dto) {
        if (dto == null) return null;
        Language language = new Language();
        language.setId(dto.getId());
        language.setName(dto.getName());
        return language;
    }
}
