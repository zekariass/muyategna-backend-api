package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.city.CityTranslationCreateDto;
import com.muyategna.backend.location.dto.city.CityTranslationDto;
import com.muyategna.backend.location.dto.city.CityTranslationMinimalDto;
import com.muyategna.backend.location.dto.city.CityTranslationUpdateDto;
import com.muyategna.backend.location.entity.CityTranslation;

public final class CityTranslationMapper {

    /**
     * Converts a {@link CityTranslation} to a {@link CityTranslationDto}.
     *
     * <p>If the given city translation is null, this method returns null.
     *
     * @param translation the city translation to convert
     * @return the converted city translation DTO
     */
    public static CityTranslationDto toDto(CityTranslation translation) {
        if (translation == null) {
            return null;
        }

        CityTranslationDto dto = new CityTranslationDto();
        dto.setId(translation.getId());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        dto.setCreatedAt(translation.getCreatedAt());
        dto.setUpdatedAt(translation.getUpdatedAt());
        dto.setCityId(translation.getCity().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        return dto;
    }


    /**
     * Converts a {@link CityTranslation} to a {@link CityTranslationCreateDto}.
     *
     * <p>If the given city translation is null, this method returns null.
     *
     * @param translation the city translation to convert
     * @return the converted city translation create DTO
     */
    public static CityTranslationCreateDto toCreateDto(CityTranslation translation) {
        if (translation == null) {
            return null;
        }

        CityTranslationCreateDto dto = new CityTranslationCreateDto();
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        dto.setCityId(translation.getCity().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        return dto;
    }


    /**
     * Converts a {@link CityTranslation} to a {@link CityTranslationUpdateDto}.
     *
     * <p>If the given city translation is null, this method returns null.
     *
     * @param translation the city translation to convert
     * @return the converted city translation update DTO
     */

    public static CityTranslationUpdateDto toUpdateDto(CityTranslation translation) {
        if (translation == null) {
            return null;
        }

        CityTranslationUpdateDto dto = new CityTranslationUpdateDto();
        dto.setId(translation.getId());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        dto.setCityId(translation.getCity().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        return dto;
    }


    /**
     * Maps a {@link CityTranslation} to a {@link CityTranslationMinimalDto}.
     *
     * <p>If the given city translation is null, this method returns null.
     *
     * @param translation the city translation to map
     * @return the mapped city translation minimal DTO
     */
    public static CityTranslationMinimalDto toMinimalDto(CityTranslation translation) {
        if (translation == null) {
            return null;
        }

        CityTranslationMinimalDto dto = new CityTranslationMinimalDto();
        dto.setId(translation.getId());
        dto.setCityId(translation.getCity().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        dto.setName(translation.getName());
        return dto;
    }

}
