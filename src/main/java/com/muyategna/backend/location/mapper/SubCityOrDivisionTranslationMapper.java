package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationCreateDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationMinimalDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationUpdateDto;
import com.muyategna.backend.location.entity.SubCityOrDivision;
import com.muyategna.backend.location.entity.SubCityOrDivisionTranslation;
import org.springframework.stereotype.Component;

@Component
public final class SubCityOrDivisionTranslationMapper {

    /**
     * Converts a {@link SubCityOrDivisionTranslation} entity to a {@link SubCityOrDivisionTranslationDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param translation the entity to convert
     * @return the converted DTO
     */
    public static SubCityOrDivisionTranslationDto toDto(SubCityOrDivisionTranslation translation) {
        if (translation == null) {
            return null;
        }
        SubCityOrDivisionTranslationDto dto = new SubCityOrDivisionTranslationDto();
        dto.setId(translation.getId());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        dto.setSubCityOrDivisionId(translation.getSubCityOrDivision().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        dto.setCreatedAt(translation.getCreatedAt());
        dto.setUpdatedAt(translation.getUpdatedAt());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivisionTranslation} entity to a {@link SubCityOrDivisionTranslationCreateDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param translation the entity to convert
     * @return the converted DTO
     */
    public static SubCityOrDivisionTranslationCreateDto toCreateDto(SubCityOrDivisionTranslation translation) {
        if (translation == null) {
            return null;
        }
        SubCityOrDivisionTranslationCreateDto dto = new SubCityOrDivisionTranslationCreateDto();
        dto.setSubCityOrDivisionId(translation.getSubCityOrDivision().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        dto.setSubCityOrDivisionId(translation.getSubCityOrDivision().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivisionTranslation} entity to a {@link SubCityOrDivisionTranslationUpdateDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param translation the entity to convert
     * @return the converted DTO
     */
    public static SubCityOrDivisionTranslationUpdateDto toUpdateDto(SubCityOrDivisionTranslation translation) {
        if (translation == null) {
            return null;
        }
        SubCityOrDivisionTranslationUpdateDto dto = new SubCityOrDivisionTranslationUpdateDto();
        dto.setId(translation.getId());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        dto.setSubCityOrDivisionId(translation.getSubCityOrDivision().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivisionTranslation} entity to a {@link SubCityOrDivisionTranslationMinimalDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param translation the entity to convert
     * @return the converted minimal DTO
     */
    public static SubCityOrDivisionTranslationMinimalDto toMinimalDto(SubCityOrDivisionTranslation translation) {
        if (translation == null) {
            return null;
        }
        SubCityOrDivisionTranslationMinimalDto dto = new SubCityOrDivisionTranslationMinimalDto();
        dto.setId(translation.getId());
        dto.setSubCityOrDivisionId(translation.getSubCityOrDivision().getId());
        dto.setLanguageId(translation.getLanguage().getId());
        dto.setName(translation.getName());
        return dto;
    }

    public static SubCityOrDivisionTranslation toEntity(SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto, SubCityOrDivision subCityOrDivision) {
        if (subCityOrDivisionTranslationDto == null) {
            return null;
        }
        SubCityOrDivisionTranslation subCityOrDivisionTranslation = new SubCityOrDivisionTranslation();
        subCityOrDivisionTranslation.setId(subCityOrDivisionTranslationDto.getId());
        subCityOrDivisionTranslation.setName(subCityOrDivisionTranslationDto.getName());
        subCityOrDivisionTranslation.setDescription(subCityOrDivisionTranslationDto.getDescription());
        subCityOrDivisionTranslation.setSubCityOrDivision(subCityOrDivision);
        return subCityOrDivisionTranslation;
    }
}
