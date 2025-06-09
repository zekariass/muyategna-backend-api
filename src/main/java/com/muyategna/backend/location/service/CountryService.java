package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.country.CountryCreateDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.country.CountryLocalizedDto;
import com.muyategna.backend.location.dto.country.CountryUpdateDto;

import java.util.List;

public interface CountryService {
    CountryDto getGlobalCountry();

    CountryLocalizedDto getCurrentCountryPublic(CountryDto countryDto, LanguageDto languageDto);

    CountryDto getCountryById(Long countryId);

    CountryLocalizedDto getLocalizedCountryByIsoCode2(String countryIsoCode2);

    CountryDto getCountryByIsoCode2(String countryIsoCode2);

    CountryDto saveCountry(CountryCreateDto createDto);

    List<CountryDto> getAllCountries();

    CountryDto updateCountry(Long countryId, CountryUpdateDto updateDto);

    void deleteCountry(Long countryId);
}
