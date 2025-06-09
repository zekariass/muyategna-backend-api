package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.region.RegionTranslationCreateDto;
import com.muyategna.backend.location.dto.region.RegionTranslationDto;
import com.muyategna.backend.location.dto.region.RegionTranslationMinimalDto;
import com.muyategna.backend.location.dto.region.RegionTranslationUpdateDto;
import com.muyategna.backend.location.entity.RegionTranslation;
import org.springframework.stereotype.Component;

@Component
public final class RegionTranslationMapper {

//    public static RegionTranslationDto toDto(RegionTranslation regionTranslation) {
//        if (regionTranslation == null) {
//            return null;
//        }
//        RegionTranslationDto dto = new RegionTranslationDto();
//        dto.setId(regionTranslation.getId());
//        dto.setName(regionTranslation.getName());
//        dto.setRegion(regionTranslation.getRegion().getId());
//        dto.setDescription(regionTranslation.getDescription());
//        dto.setLanguage(regionTranslation.getLanguage().getId());
//        dto.setCreatedAt(regionTranslation.getCreatedAt());
//        dto.setUpdatedAt(regionTranslation.getUpdatedAt());
//        return dto;
//    }
//
//    public static RegionTranslation toEntity(RegionTranslationDto dto, Region region, Language language) {
//        if (dto == null) {
//            return null;
//        }
//        RegionTranslation regionTranslation = new RegionTranslation();
//        regionTranslation.setId(dto.getId());
//        regionTranslation.setRegion(region);
//        regionTranslation.setName(dto.getName());
//        regionTranslation.setLanguage(language);
//        regionTranslation.setDescription(dto.getDescription());
//        regionTranslation.setCreatedAt(dto.getCreatedAt());
//        regionTranslation.setUpdatedAt(dto.getUpdatedAt());
//        return regionTranslation;
//    }


    /**
     * Converts a {@link RegionTranslation} to a {@link RegionTranslationMinimalDto}.
     *
     * <p>If the given region translation is null, this method returns null.
     *
     * @param regionTranslation the region translation to convert
     * @return the converted region translation minimal DTO
     */
    public static RegionTranslationMinimalDto toMinimalDto(RegionTranslation regionTranslation) {
        if (regionTranslation == null) {
            return null;
        }
        RegionTranslationMinimalDto dto = new RegionTranslationMinimalDto();
        dto.setId(regionTranslation.getId());
        dto.setRegionId(regionTranslation.getRegion().getId());
        dto.setLanguageId(regionTranslation.getLanguage().getId());
        dto.setName(regionTranslation.getName());
        return dto;
    }


    /**
     * Converts a {@link RegionTranslation} to a {@link RegionTranslationCreateDto}.
     *
     * <p>If the given region translation is null, this method returns null.
     *
     * @param regionTranslation the region translation to convert
     * @return the converted region translation create DTO
     */
    public static RegionTranslationCreateDto toCreateDto(RegionTranslation regionTranslation) {
        if (regionTranslation == null) {
            return null;
        }
        RegionTranslationCreateDto dto = new RegionTranslationCreateDto();
        dto.setRegionId(regionTranslation.getRegion().getId());
        dto.setLanguageId(regionTranslation.getLanguage().getId());
        dto.setName(regionTranslation.getName());
        dto.setDescription(regionTranslation.getDescription());
        return dto;
    }


    /**
     * Converts a {@link RegionTranslation} to a {@link RegionTranslationUpdateDto}.
     *
     * <p>If the given region translation is null, this method returns null.
     *
     * @param regionTranslation the region translation to convert
     * @return the converted region translation update DTO
     */
    public static RegionTranslationUpdateDto toUpdateDto(RegionTranslation regionTranslation) {
        if (regionTranslation == null) {
            return null;
        }
        RegionTranslationUpdateDto dto = new RegionTranslationUpdateDto();
        dto.setId(regionTranslation.getId());
        dto.setRegionId(regionTranslation.getRegion().getId());
        dto.setLanguageId(regionTranslation.getLanguage().getId());
        dto.setName(regionTranslation.getName());
        dto.setDescription(regionTranslation.getDescription());
        return dto;
    }


    /**
     * Converts a {@link RegionTranslation} to a {@link RegionTranslationDto}.
     *
     * <p>If the given region translation is null, this method returns null.
     *
     * @param regionTranslation the region translation to convert
     * @return the converted region translation DTO
     */
    public static RegionTranslationDto toDto(RegionTranslation regionTranslation) {
        if (regionTranslation == null) {
            return null;
        }
        RegionTranslationDto dto = new RegionTranslationDto();
        dto.setId(regionTranslation.getId());
        dto.setRegionId(regionTranslation.getRegion().getId());
        dto.setLanguageId(regionTranslation.getLanguage().getId());
        dto.setName(regionTranslation.getName());
        dto.setDescription(regionTranslation.getDescription());
        dto.setCreatedAt(regionTranslation.getCreatedAt());
        dto.setUpdatedAt(regionTranslation.getUpdatedAt());
        return dto;
    }
}
