package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.city.CityDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.RegionDto;
import com.muyategna.backend.location.dto.sub_city_or_division.*;
import com.muyategna.backend.location.entity.SubCityOrDivision;
import org.springframework.stereotype.Component;

@Component
public final class SubCityOrDivisionMapper {

//    public static SubCityOrDivisionDto toDto(SubCityOrDivision subCityOrDivision, SubCityOrDivisionView translation) {
//        if (subCityOrDivision == null || translation == null) {
//            return null;
//        }
//        SubCityOrDivisionDto dto = new SubCityOrDivisionDto();
//        dto.setId(subCityOrDivision.getId());
//        dto.setName(translation.getName());
//        dto.setParentCityName(subCityOrDivision.getCity().getName());
//        dto.setDescription(translation.getDescription());
//        return dto;
//    }


    /**
     * Converts a {@link SubCityOrDivision} entity to a {@link SubCityOrDivisionDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param subCityOrDivision the entity to convert
     * @return the converted DTO
     */
    public static SubCityOrDivisionDto toDto(SubCityOrDivision subCityOrDivision) {
        if (subCityOrDivision == null) {
            return null;
        }
        SubCityOrDivisionDto dto = new SubCityOrDivisionDto();
        dto.setId(subCityOrDivision.getId());
        dto.setName(subCityOrDivision.getName());
        dto.setCityId(subCityOrDivision.getCity().getId());
        dto.setDescription(subCityOrDivision.getDescription());
        dto.setCreatedAt(subCityOrDivision.getCreatedAt());
        dto.setUpdatedAt(subCityOrDivision.getUpdatedAt());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivision} entity to a {@link SubCityOrDivisionCreateDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param subCityOrDivision the entity to convert
     * @return the converted DTO
     */
    public static SubCityOrDivisionCreateDto toCreateDto(SubCityOrDivision subCityOrDivision) {
        if (subCityOrDivision == null) {
            return null;
        }
        SubCityOrDivisionCreateDto dto = new SubCityOrDivisionCreateDto();
        dto.setCityId(subCityOrDivision.getCity().getId());
        dto.setName(subCityOrDivision.getName());
        dto.setDescription(subCityOrDivision.getDescription());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivision} entity to a {@link SubCityOrDivisionUpdateDto}.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param subCityOrDivision the entity to convert
     * @return the converted DTO
     */
    public static SubCityOrDivisionUpdateDto toUpdateDto(SubCityOrDivision subCityOrDivision) {
        if (subCityOrDivision == null) {
            return null;
        }
        SubCityOrDivisionUpdateDto dto = new SubCityOrDivisionUpdateDto();
        dto.setId(subCityOrDivision.getId());
        dto.setCityId(subCityOrDivision.getCity().getId());
        dto.setName(subCityOrDivision.getName());
        dto.setDescription(subCityOrDivision.getDescription());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivision} entity to a {@link SubCityOrDivisionMinimalDto}.
     *
     * <p>This method only maps the id, city id, and name of the given entity.
     *
     * <p>If the given entity is null, this method returns null.
     *
     * @param subCityOrDivision the entity to convert
     * @return the converted minimal DTO
     */
    public static SubCityOrDivisionMinimalDto toMinimalDto(SubCityOrDivision subCityOrDivision) {
        if (subCityOrDivision == null) {
            return null;
        }
        SubCityOrDivisionMinimalDto dto = new SubCityOrDivisionMinimalDto();
        dto.setId(subCityOrDivision.getId());
        dto.setCityId(subCityOrDivision.getCity().getId());
        dto.setName(subCityOrDivision.getName());
        return dto;
    }


    /**
     * Converts a {@link SubCityOrDivisionCreateDto} to a {@link SubCityOrDivision} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted city entity
     */
    public static SubCityOrDivision toEntity(SubCityOrDivisionCreateDto dto, CityDto cityDto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        SubCityOrDivision subCityOrDivision = new SubCityOrDivision();
        subCityOrDivision.setCity(CityMapper.toEntity(cityDto, regionDto, countryDto));
        subCityOrDivision.setName(dto.getName());
        subCityOrDivision.setDescription(dto.getDescription());
        return subCityOrDivision;
    }


    /**
     * Converts a {@link SubCityOrDivisionUpdateDto} to a {@link SubCityOrDivision} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted city entity
     */
    public static SubCityOrDivision toEntity(SubCityOrDivisionUpdateDto dto, CityDto cityDto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        SubCityOrDivision subCityOrDivision = new SubCityOrDivision();
        subCityOrDivision.setId(dto.getId());
        subCityOrDivision.setName(dto.getName());
        subCityOrDivision.setDescription(dto.getDescription());
        subCityOrDivision.setCity(CityMapper.toEntity(cityDto, regionDto, countryDto));
        return subCityOrDivision;
    }


    /**
     * Converts a {@link SubCityOrDivisionMinimalDto} to a {@link SubCityOrDivision} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the minimal DTO to convert
     * @return the converted sub city or division entity
     */
    public static SubCityOrDivision toEntity(SubCityOrDivisionMinimalDto dto, CityDto cityDto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        SubCityOrDivision subCityOrDivision = new SubCityOrDivision();
        subCityOrDivision.setId(dto.getId());
        subCityOrDivision.setName(dto.getName());
        subCityOrDivision.setCity(CityMapper.toEntity(cityDto, regionDto, countryDto));
        return subCityOrDivision;
    }


    /**
     * Converts a {@link SubCityOrDivisionDto} to a {@link SubCityOrDivision} entity.
     *
     * <p>If the given DTO is null, this method returns null.
     *
     * @param dto the DTO to convert
     * @return the converted sub city or division entity
     */
    public static SubCityOrDivision toEntity(SubCityOrDivisionDto dto, CityDto cityDto, RegionDto regionDto, CountryDto countryDto) {
        if (dto == null) {
            return null;
        }
        SubCityOrDivision subCityOrDivision = new SubCityOrDivision();
        subCityOrDivision.setId(dto.getId());
        subCityOrDivision.setCity(CityMapper.toEntity(cityDto, regionDto, countryDto));
        subCityOrDivision.setName(dto.getName());
        subCityOrDivision.setDescription(dto.getDescription());
        subCityOrDivision.setCreatedAt(dto.getCreatedAt());
        subCityOrDivision.setUpdatedAt(dto.getUpdatedAt());
        return subCityOrDivision;
    }

    public static SubCityOrDivisionLocalizedDto toLocalizedDto(SubCityOrDivision subCityOrDivision, SubCityOrDivisionTranslationDto translation) {
        if (subCityOrDivision == null) return null; // null check elf

        SubCityOrDivisionLocalizedDto dto = new SubCityOrDivisionLocalizedDto(); // null check elfb

        if (translation == null) {
            dto.setId(subCityOrDivision.getId());
            dto.setCityId(subCityOrDivision.getCity().getId());
            dto.setName(subCityOrDivision.getName());
            dto.setDescription(subCityOrDivision.getDescription());
            return dto;
        }

        dto.setId(subCityOrDivision.getId());
        dto.setCityId(subCityOrDivision.getCity().getId());
        dto.setName(translation.getName());
        dto.setDescription(translation.getDescription());
        return dto;


    }
}
