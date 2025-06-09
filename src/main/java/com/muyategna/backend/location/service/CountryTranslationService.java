package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.country.CountryTranslationCreateDto;
import com.muyategna.backend.location.dto.country.CountryTranslationDto;
import com.muyategna.backend.location.dto.country.CountryTranslationUpdateDto;

import java.util.List;

public interface CountryTranslationService {
    CountryTranslationDto getCountryTranslationById(Long translationId);

    List<CountryTranslationDto> getCountryTranslationsByCountryId(Long countryId);

    CountryTranslationDto getCountryTranslationByCountryIdAndLanguageId(Long countryId, Long languageId);

    CountryTranslationDto addNewCountryTranslation(CountryTranslationCreateDto countryTranslationDto);

    CountryTranslationDto updateCountryTranslation(Long translationId, CountryTranslationUpdateDto countryTranslationDto);

    void deleteCountryTranslation(Long translationId);

    CountryTranslationDto getCountryTranslationByCountryIdAndLocale(Long countryId, String locale);
    
    CountryTranslationDto findCountryTranslationByCountryAndLanguagePublic(CountryDto countryDto, LanguageDto languageDto);
}
