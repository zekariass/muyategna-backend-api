package com.muyategna.backend.location.service;

import com.muyategna.backend.location.dto.region.RegionTranslationCreateDto;
import com.muyategna.backend.location.dto.region.RegionTranslationDto;
import com.muyategna.backend.location.dto.region.RegionTranslationUpdateDto;

import java.util.List;

public interface RegionTranslationService {
    RegionTranslationDto getRegionTranslationById(Long translationId);

    List<RegionTranslationDto> getRegionTranslationsByRegionId(Long regionId);

    RegionTranslationDto getRegionTranslationByRegionIdAndLanguageId(Long regionId, Long languageId);

    RegionTranslationDto getRegionTranslationByRegionIdAndLocale(Long regionId, String locale);

    RegionTranslationDto addNewRegionTranslation(RegionTranslationCreateDto regionTranslationDto);

    RegionTranslationDto updateRegionTranslation(Long translationId, RegionTranslationUpdateDto regionTranslationDto);

    void deleteRegionTranslation(Long translationId);


}
