package com.muyategna.backend.common.service;

import com.muyategna.backend.common.dto.language.LanguageCreateDto;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.dto.language.LanguageUpdateDto;
import com.muyategna.backend.location.dto.country.CountryDto;

import java.util.List;
import java.util.Optional;

public interface LanguageService {

    LanguageDto getLanguageById(Long id);

    Optional<LanguageDto> getDefaultLanguageForCountry(CountryDto countryDto);

    List<LanguageDto> getAllLanguagesByCountryPublic(CountryDto countryDto);

    LanguageDto getLanguageByLocale(String locale);

    LanguageDto getGlobalLanguage();

    List<LanguageDto> getAllLanguagesForAdmin();

    LanguageDto addNewLanguage(LanguageCreateDto languageCreateDto);

    LanguageDto updateLanguage(Long languageId, LanguageUpdateDto languageCreateDto);

    void deleteLanguage(Long languageId);

    List<LanguageDto> getLanguagesByCountryId(Long countryId);
}
