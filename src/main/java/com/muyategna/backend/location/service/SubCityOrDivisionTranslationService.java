package com.muyategna.backend.location.service;

import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationCreateDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SubCityOrDivisionTranslationService {
    SubCityOrDivisionTranslationDto getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLanguageId(Long subCityOrDivisionId, long languageId);

    List<SubCityOrDivisionTranslationDto> getSubCityOrDivisionTranslationsBySubCityOrDivisionIdsAndLanguageId(List<Long> subCityOrDivisionIds, long id);

    Page<SubCityOrDivisionTranslationDto> getSubCityOrDivisionTranslationsBySubCityOrDivisionId(Long subCityOrDivisionId, Pageable pageable);

    SubCityOrDivisionTranslationDto getSubCityOrDivisionTranslationById(Long translationId);

    SubCityOrDivisionTranslationDto getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLocale(Long subCityOrDivisionId, String locale);

    SubCityOrDivisionTranslationDto updateSubCityOrDivisionTranslation(Long translationId, SubCityOrDivisionTranslationUpdateDto subCityOrDivisionTranslationDto);

    SubCityOrDivisionTranslationDto addNewSubCityOrDivisionTranslation(SubCityOrDivisionTranslationCreateDto subCityOrDivisionTranslationDto);

    void deleteSubCityOrDivisionTranslation(Long translationId);
}
