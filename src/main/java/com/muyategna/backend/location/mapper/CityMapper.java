package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.city.*;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.RegionDto;
import com.muyategna.backend.location.entity.City;
import com.muyategna.backend.location.entity.CityTranslation;

public final class CityMapper {

    /**
     * Maps a {@link City} to a {@link CityMinimalDto}.
     *
     * <p>If the given city is null, this method returns null.
     *
     * @param city the city to map
     * @return the mapped city minimal DTO
     */
    public static CityMinimalDto toMinimalDto(City city) {
        if (city == null) {
            return null;
        }

        CityMinimalDto dto = new CityMinimalDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setRegionId(city.getRegion().getId());
        return dto;
    }

    /**
     * Converts a {@link City} entity to a {@link CityCreateDto}.
     *
     * <p>If the given city is null, this method returns null.
     *
     * @param city the city entity to convert
     * @return the converted city create DTO
     */

    public static CityCreateDto toCreateDto(City city) {
        if (city == null) {
            return null;
        }

        CityCreateDto dto = new CityCreateDto();
        dto.setName(city.getName());
        dto.setRegionId(city.getRegion().getId());
        dto.setDescription(city.getDescription());
        return dto;
    }

    /**
     * Converts a {@link City} entity to a {@link CityUpdateDto}.
     *
     * <p>If the given city is null, this method returns null.
     *
     * @param city the city entity to convert
     * @return the converted city update DTO
     */

    public static CityUpdateDto toUpdateDto(City city) {
        if (city == null) {
            return null;
        }

        CityUpdateDto dto = new CityUpdateDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setRegionId(city.getRegion().getId());
        dto.setDescription(city.getDescription());
        return dto;
    }


    /**
     * Converts a {@link City} entity to a {@link CityDto}.
     *
     * <p>If the given city is null, this method returns null.
     *
     * @param city the city entity to convert
     * @return the converted city DTO
     */

    public static CityDto toDto(City city) {
        if (city == null) {
            return null;
        }

        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setDescription(city.getDescription());
        dto.setRegionId(city.getRegion().getId());
        dto.setCreatedAt(city.getCreatedAt());
        dto.setUpdatedAt(city.getUpdatedAt());
        return dto;
    }


    /**
     * Converts a {@link CityMinimalDto} to a {@link City} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted city entity
     */
    public static City toEntity(CityMinimalDto dto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        City city = new City();
        city.setId(dto.getId());
        city.setRegion(RegionMapper.toEntity(regionDto, countryDto));
        city.setName(dto.getName());
        return city;
    }


    /**
     * Converts a {@link CityCreateDto} to a {@link City} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted city entity
     */

    public static City toEntity(CityCreateDto dto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        City city = new City();
        city.setName(dto.getName());
        city.setDescription(dto.getDescription());
        city.setRegion(RegionMapper.toEntity(regionDto, countryDto));
        return city;
    }


    /**
     * Converts a {@link CityUpdateDto} to a {@link City} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted city entity
     */
    public static City toEntity(CityUpdateDto dto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        City city = new City();
        city.setId(dto.getId());
        city.setRegion(RegionMapper.toEntity(regionDto, countryDto));
        city.setName(dto.getName());
        city.setDescription(dto.getDescription());
        return city;
    }

    /**
     * Converts a {@link CityDto} to a {@link City} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted city entity
     */
    public static City toEntity(CityDto dto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }

        City city = new City();
        city.setId(dto.getId());
        city.setName(dto.getName());
        city.setDescription(dto.getDescription());
        city.setRegion(RegionMapper.toEntity(regionDto, countryDto));
        city.setCreatedAt(dto.getCreatedAt());
        city.setUpdatedAt(dto.getUpdatedAt());
        return city;
    }

    public static CityLocalizedDto toLocalizedDto(City city, CityTranslation cityTranslation) {
        if (city == null) {
            return null;
        }
        CityLocalizedDto dto = new CityLocalizedDto();
        dto.setId(city.getId());
        dto.setName(cityTranslation.getName());
        dto.setRegionId(city.getRegion().getId());
        dto.setDescription(cityTranslation.getDescription());
        return dto;
    }

}
