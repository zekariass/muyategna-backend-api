package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.*;
import com.muyategna.backend.location.entity.Region;
import com.muyategna.backend.location.entity.RegionTranslation;

public final class RegionMapper {

    /**
     * Maps a Region entity to a RegionMinimalDto.
     *
     * <p>If the given region is null, this method returns null.
     *
     * @param region the region to map
     * @return the mapped region minimal DTO
     */
    public static RegionMinimalDto toMinimalDto(Region region) {
        if (region == null) {
            return null;
        }
        RegionMinimalDto dto = new RegionMinimalDto();
        dto.setId(region.getId());
        dto.setCountryId(region.getCountry().getId());
        dto.setName(region.getName());
        return dto;
    }


    public static RegionCreateDto toCreateDto(Region region) {
        if (region == null) {
            return null;
        }
        RegionCreateDto dto = new RegionCreateDto();
        dto.setName(region.getName());
        dto.setDescription(region.getDescription());
        dto.setCountryId(region.getCountry().getId());
        return dto;
    }


    public static RegionUpdateDto toUpdateDto(Region region) {
        if (region == null) {
            return null;
        }
        RegionUpdateDto dto = new RegionUpdateDto();
        dto.setId(region.getId());
        dto.setName(region.getName());
        dto.setDescription(region.getDescription());
        dto.setCountryId(region.getCountry().getId());
        return dto;
    }

    public static RegionDto toDto(Region region) {
        if (region == null) {
            return null;
        }
        RegionDto dto = new RegionDto();
        dto.setId(region.getId());
        dto.setName(region.getName());
        dto.setDescription(region.getDescription());
        dto.setCountryId(region.getCountry().getId());
        dto.setCreatedAt(region.getCreatedAt());
        dto.setUpdatedAt(region.getUpdatedAt());
        return dto;
    }


    public static Region toEntity(RegionCreateDto dto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        Region region = new Region();
        region.setName(dto.getName());
        region.setDescription(dto.getDescription());
        region.setCountry(CountryMapper.toEntity(countryDto));
        return region;
    }


    public static Region toEntity(RegionUpdateDto dto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        Region region = new Region();
        region.setId(dto.getId());
        region.setName(dto.getName());
        region.setDescription(dto.getDescription());
        region.setCountry(CountryMapper.toEntity(countryDto));
        return region;
    }


    public static Region toEntity(RegionDto dto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        Region region = new Region();
        region.setId(dto.getId());
        region.setName(dto.getName());
        region.setDescription(dto.getDescription());
        region.setCountry(CountryMapper.toEntity(countryDto));
        region.setCreatedAt(dto.getCreatedAt());
        region.setUpdatedAt(dto.getUpdatedAt());
        return region;
    }

    public static Region toEntity(RegionMinimalDto dto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        Region region = new Region();
        region.setId(dto.getId());
        region.setName(dto.getName());
        region.setCountry(CountryMapper.toEntity(countryDto));
        return region;
    }

    public static RegionLocalizedDto toLocalizedDto(Region region, RegionTranslation regionTranslation) {
        if (region == null) {
            return null;
        }

        RegionLocalizedDto dto = new RegionLocalizedDto();

        if (regionTranslation == null) {

            dto.setId(region.getId());
            dto.setName(region.getName());
            dto.setDescription(region.getDescription());
            dto.setCountryId(region.getCountry().getId());
            return dto;
        }

        dto.setId(region.getId());
        dto.setName(regionTranslation.getName());
        dto.setDescription(regionTranslation.getDescription());
        dto.setCountryId(region.getCountry().getId());
        return dto;
    }
}
