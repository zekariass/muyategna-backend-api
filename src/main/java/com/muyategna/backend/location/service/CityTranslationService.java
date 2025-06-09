package com.muyategna.backend.location.service;

import com.muyategna.backend.location.dto.city.CityTranslationCreateDto;
import com.muyategna.backend.location.dto.city.CityTranslationDto;
import com.muyategna.backend.location.dto.city.CityTranslationUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityTranslationService {
    Page<CityTranslationDto> getCityTranslationsByCityId(Long cityId, Pageable pageable);

    CityTranslationDto getCityTranslationById(Long translationId);

    CityTranslationDto getCityTranslationByCityIdAndLanguageId(Long cityId, Long languageId);

    CityTranslationDto getCityTranslationByCityIdAndLocale(Long cityId, String locale);

    CityTranslationDto updateCityTranslation(Long translationId, CityTranslationUpdateDto cityTranslationDto);

    CityTranslationDto addNewCityTranslation(CityTranslationCreateDto cityTranslationDto);

    void deleteCityTranslation(Long translationId);
}
