package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.RegionCreateDto;
import com.muyategna.backend.location.dto.region.RegionDto;
import com.muyategna.backend.location.dto.region.RegionLocalizedDto;
import com.muyategna.backend.location.dto.region.RegionUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegionService {
    RegionLocalizedDto getRegionByIdAndCountryAndLanguage(Long regionId, CountryDto country, LanguageDto language);

    List<RegionLocalizedDto> getAllRegionsByCountryAndLanguage(CountryDto country, LanguageDto language);

    RegionDto updateRegion(Long regionId, RegionUpdateDto regionUpdateDto);

    List<RegionDto> getAllRegions();

    List<RegionDto> getRegionsByCountry(Long countryId);

    Page<RegionDto> getRegionsByCountry(Long countryId, Pageable pageable);

    RegionDto getRegionById(Long regionId);

    RegionDto createRegion(RegionCreateDto regionCreateDto);

    void deleteRegion(Long regionId);
}
