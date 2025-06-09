package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.city.CityCreateDto;
import com.muyategna.backend.location.dto.city.CityDto;
import com.muyategna.backend.location.dto.city.CityLocalizedDto;
import com.muyategna.backend.location.dto.city.CityUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {
    CityLocalizedDto getCityById(Long cityId, LanguageDto languageDto);

    List<CityLocalizedDto> getAllCitiesByRegion(Long regionId, long languageId);

    Page<CityDto> getAllCitiesByRegionForAdmin(Long regionId, Pageable pageable);

    CityDto getCityByIdForAdmin(Long cityId);

    Page<CityDto> getAllCitiesForAdmin(Pageable pageable);

    CityDto addNewCityByAdmin(CityCreateDto cityCreateDto);

    CityDto updateCityByAdmin(Long cityId, CityUpdateDto cityUpdateDto);

    void deleteCityByAdmin(Long cityId);

    CityDto getCityById(Long cityId);
}
