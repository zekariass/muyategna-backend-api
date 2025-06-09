package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionCreateDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionLocalizedDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubCityOrDivisionService {
    SubCityOrDivisionDto getSubCityOrDivisionByIdPublic(Long subCityOrDivisionId);

    SubCityOrDivisionLocalizedDto getSubCityOrDivisionByIdPublic(Long subCityOrDivisionId, LanguageDto language);

    List<SubCityOrDivisionLocalizedDto> getSubCityOrDivisionByCityIdPublic(Long cityId, LanguageDto languageDto);

    SubCityOrDivisionDto getSubCityOrDivisionById(Long subCityOrDivisionId);

    Page<SubCityOrDivisionDto> getSubCityOrDivisionByCityId(Long cityId, Pageable pageable);

    Page<SubCityOrDivisionDto> getAllSubCityOrDivisions(Pageable pageable);

    SubCityOrDivisionDto updateSubCityOrDivisionByAdmin(Long subCityOrDivisionId, SubCityOrDivisionUpdateDto subCityOrDivisionDto);

    SubCityOrDivisionDto addNewSubCityOrDivisionByAdmin(SubCityOrDivisionCreateDto subCityOrDivisionDto);

    void deleteSubCityOrDivisionByAdmin(Long subCityOrDivisionId);
}
