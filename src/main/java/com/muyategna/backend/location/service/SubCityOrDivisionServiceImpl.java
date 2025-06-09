package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.sub_city_or_division.*;
import com.muyategna.backend.location.entity.City;
import com.muyategna.backend.location.entity.SubCityOrDivision;
import com.muyategna.backend.location.mapper.SubCityOrDivisionMapper;
import com.muyategna.backend.location.repository.CityRepository;
import com.muyategna.backend.location.repository.SubCityOrDivisionRepository;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubCityOrDivisionServiceImpl implements SubCityOrDivisionService {

    private final SubCityOrDivisionRepository subCityOrDivisionRepository;
    private final SubCityOrDivisionTranslationService subCityOrDivisionTranslationService;
    private final LanguageService languageService;
    private final CityRepository cityRepository;

    @Autowired
    public SubCityOrDivisionServiceImpl(SubCityOrDivisionRepository subCityOrDivisionRepository, SubCityOrDivisionTranslationService subCityOrDivisionTranslationService, LanguageService languageService, CityRepository cityRepository) {
        this.subCityOrDivisionRepository = subCityOrDivisionRepository;
        this.subCityOrDivisionTranslationService = subCityOrDivisionTranslationService;
        this.languageService = languageService;
        this.cityRepository = cityRepository;
    }


    /**
     * Retrieves a sub-city or division by its ID and translates it to the current language.
     *
     * @param subCityOrDivisionId the ID of the sub-city or division to be retrieved
     * @return a SubCityOrDivisionDto object containing the details of the sub-city or division
     */
    @Override
    public SubCityOrDivisionDto getSubCityOrDivisionByIdPublic(Long subCityOrDivisionId) {
        log.info("Retrieving sub-city or division with ID: {}", subCityOrDivisionId);
        SubCityOrDivisionDto subCityOrDivisionDto = subCityOrDivisionRepository
                .findById(subCityOrDivisionId)
                .map(SubCityOrDivisionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sub city or division not found for ID: " + subCityOrDivisionId));
        log.info("Sub-city or division retrieved successfully: {}", subCityOrDivisionDto);
        return subCityOrDivisionDto;
    }


    /**
     * Retrieves a sub-city or division by its ID and translates it to the specified language.
     *
     * @param subCityOrDivisionId the ID of the sub-city or division to be retrieved
     * @param language            the language in which the sub-city or division should be translated
     * @return a SubCityOrDivisionLocalizedDto object containing the details of the sub-city or division in the specified language
     */
    @Override
    public SubCityOrDivisionLocalizedDto getSubCityOrDivisionByIdPublic(Long subCityOrDivisionId, LanguageDto language) {
        log.info("Retrieving sub-city or division with ID: {} for language: {}", subCityOrDivisionId, language.getName());
        SubCityOrDivision subCityOrDivision = subCityOrDivisionRepository
                .findById(subCityOrDivisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sub city or division not found for ID: " + subCityOrDivisionId));

        if (!subCityOrDivision
                .getCity()
                .getRegion()
                .getCountry()
                .getCountryIsoCode2()
                .equals(CountryContextHolder
                        .getCountry()
                        .getCountryIsoCode2())) {
            log.error("Sub city or division with ID: {} does not belong to country with name: {}", subCityOrDivisionId, CountryContextHolder.getCountry().getName());
            throw new ResourceNotFoundException("Sub city or division with ID: " + subCityOrDivisionId + " does not belong to country with name: " + CountryContextHolder.getCountry().getName());
        }

        SubCityOrDivisionTranslationDto translation = subCityOrDivisionTranslationService.getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLanguageId(subCityOrDivisionId, language.getId());

        if (translation == null) {
            log.info("No translation found for sub-city or division ID: {} in language: {}", subCityOrDivisionId, language.getName());
            // Fallback to default language if no translation is found
            log.info("Falling back to global language for sub-city or division ID: {}", subCityOrDivisionId);
            LanguageDto defaultLanguage = languageService.getGlobalLanguage();
            translation = subCityOrDivisionTranslationService.getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLanguageId(subCityOrDivisionId, defaultLanguage.getId());
        }

        if (translation == null) {
            log.error("Sub city or division translation not found for ID: {} and language ID: {}", subCityOrDivisionId, language.getId());
            throw new ResourceNotFoundException("Sub city or division translation not found for ID: " + subCityOrDivisionId + " and language ID: " + language.getId());
        }

        log.info("Sub city or division retrieved successfully: {}", subCityOrDivision);
        return SubCityOrDivisionMapper.toLocalizedDto(subCityOrDivision, translation);
    }


    /**
     * Retrieves a list of sub-cities or divisions for a given city ID and translates them to the specified language.
     *
     * @param cityId      the ID of the city for which sub-cities or divisions are to be retrieved
     * @param languageDto the language in which the sub-cities or divisions should be translated
     * @return a list of SubCityOrDivisionLocalizedDto objects containing the details of the sub-cities or divisions in the specified language
     */
    @Override
    public List<SubCityOrDivisionLocalizedDto> getSubCityOrDivisionByCityIdPublic(Long cityId, LanguageDto languageDto) {

        log.info("Retrieving sub-cities or divisions for city ID: {} in language: {}", cityId, languageDto.getName());
        List<SubCityOrDivision> subCityOrDivisions = subCityOrDivisionRepository.findByCityId(cityId);

        if (subCityOrDivisions.isEmpty()) {
            log.error("No sub-city or division found for city ID: {}", cityId);
            throw new ResourceNotFoundException("Sub city or division not found for city ID: " + cityId);
        }

        // Country isolation check
        String requestedIso = subCityOrDivisions
                .getFirst()
                .getCity()
                .getRegion()
                .getCountry()
                .getCountryIsoCode2();

        String contextIso = CountryContextHolder.getCountry().getCountryIsoCode2();

        if (!requestedIso.equals(contextIso)) {
            log.error("Sub city or division with city ID: {} does not belong to country with name: {}", cityId, CountryContextHolder.getCountry().getName());
            throw new ResourceNotFoundException("Sub city or division with city ID: " + cityId
                    + " does not belong to country with name: " + CountryContextHolder.getCountry().getName());
        }

        // Get all sub-city IDs
        List<Long> subCityOrDivisionIds = subCityOrDivisions.stream()
                .map(SubCityOrDivision::getId)
                .toList();

        // Primary translations
        List<SubCityOrDivisionTranslationDto> primaryTranslations = subCityOrDivisionTranslationService
                .getSubCityOrDivisionTranslationsBySubCityOrDivisionIdsAndLanguageId(subCityOrDivisionIds, languageDto.getId());

        // Map them safely
        Map<Long, SubCityOrDivisionTranslationDto> translationMap = primaryTranslations.stream()
                .filter(t -> t.getSubCityOrDivisionId() != null)
                .collect(Collectors.toMap(
                        SubCityOrDivisionTranslationDto::getSubCityOrDivisionId,
                        Function.identity(),
                        (existing, replacement) -> existing // handle duplicate keys gracefully
                ));

        // Find IDs without translations
        List<Long> fallbackIds = subCityOrDivisionIds.stream()
                .filter(id -> !translationMap.containsKey(id))
                .toList();

        // Fallback translations
        if (!fallbackIds.isEmpty()) {
            LanguageDto globalLanguage = languageService.getGlobalLanguage();
            List<SubCityOrDivisionTranslationDto> fallbackTranslations = subCityOrDivisionTranslationService
                    .getSubCityOrDivisionTranslationsBySubCityOrDivisionIdsAndLanguageId(fallbackIds, globalLanguage.getId());

            Map<Long, SubCityOrDivisionTranslationDto> fallbackMap = fallbackTranslations.stream()
                    .filter(t -> t.getSubCityOrDivisionId() != null)
                    .collect(Collectors.toMap(
                            SubCityOrDivisionTranslationDto::getSubCityOrDivisionId,
                            Function.identity(),
                            (existing, replacement) -> existing
                    ));

            // Merge fallback into the main map
            translationMap.putAll(fallbackMap);
        }

        // Map entities to localized DTOs
        List<SubCityOrDivisionLocalizedDto> localizedDtos = subCityOrDivisions.stream()
                .map(scd -> SubCityOrDivisionMapper.toLocalizedDto(scd, translationMap.get(scd.getId())))
                .toList();

        if (localizedDtos.isEmpty()) {
            log.error("No sub-cities or divisions found for city ID: {}", cityId);
            throw new ResourceNotFoundException("Sub city or division not found for city ID: " + cityId);
        }

        log.info("Sub-cities or divisions retrieved successfully for city ID: {}", cityId);
        return localizedDtos;
    }

    /**
     * Retrieves a sub-city or division by its ID.
     *
     * @param subCityOrDivisionId the ID of the sub-city or division to be retrieved
     * @return a SubCityOrDivisionDto object containing the details of the sub-city or division
     */
    @Override
    public SubCityOrDivisionDto getSubCityOrDivisionById(Long subCityOrDivisionId) {
        log.info("Retrieving sub-city or division with  ID: {}", subCityOrDivisionId);
        SubCityOrDivisionDto subCityOrDivisionDto = subCityOrDivisionRepository
                .findById(subCityOrDivisionId)
                .map(SubCityOrDivisionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sub city or division not found for ID: " + subCityOrDivisionId));
        log.info("Sub-city or division retrieved  successfully: {}", subCityOrDivisionDto);
        return subCityOrDivisionDto;
    }

    /**
     * Retrieves a list of sub-cities or divisions for a given city ID.
     *
     * @param cityId the ID of the city for which sub-cities or divisions are to be retrieved
     * @return a Page of SubCityOrDivisionDto objects containing the details of the sub-cities or divisions
     */
    @Override
    public Page<SubCityOrDivisionDto> getSubCityOrDivisionByCityId(Long cityId, Pageable pageable) {
        log.info("Retrieving sub-cities or divisions for city ID: {}", cityId);
        Page<SubCityOrDivisionDto> subCityOrDivisionDtoList = subCityOrDivisionRepository
                .findByCityId(cityId, pageable)
                .map(SubCityOrDivisionMapper::toDto);
        log.info("Sub cities or divisions retrieved successfully for city ID: {}", cityId);
        return subCityOrDivisionDtoList;
    }

    /**
     * Retrieves all sub-cities or divisions.
     *
     * @param pageable the pagination information
     * @return a Page of SubCityOrDivisionDto objects containing the details of all sub-cities or divisions
     */
    @Override
    public Page<SubCityOrDivisionDto> getAllSubCityOrDivisions(Pageable pageable) {
        log.info("Retrieving all sub-cities or divisions with pagination");
        Page<SubCityOrDivisionDto> subCityOrDivisionDtoList = subCityOrDivisionRepository
                .findAll(pageable)
                .map(SubCityOrDivisionMapper::toDto);
        log.info("All sub-cities or divisions retrieved successfully");
        return subCityOrDivisionDtoList;
    }

    /**
     * Updates a sub-city or division by its ID.
     *
     * @param subCityOrDivisionId  the ID of the sub-city or division to be updated
     * @param subCityOrDivisionDto the DTO containing the updated details of the sub-city or division
     * @return a SubCityOrDivisionDto object containing the updated details of the sub-city or division
     */
    @Override
    public SubCityOrDivisionDto updateSubCityOrDivisionByAdmin(Long subCityOrDivisionId, SubCityOrDivisionUpdateDto subCityOrDivisionDto) {
        log.info("Updating sub-city or division with ID: {}", subCityOrDivisionId);
        if (subCityOrDivisionDto == null) {
            log.error("Sub city or division update DTO is null for ID: {}", subCityOrDivisionId);
            throw new ResourceNotFoundException("You must provide a sub-city or division to update");
        }

        SubCityOrDivision subCityOrDivision = subCityOrDivisionRepository.findById(subCityOrDivisionId).orElseThrow(() -> new ResourceNotFoundException("Sub city or division not found for ID: " + subCityOrDivisionId));
        City city = cityRepository.findById(subCityOrDivisionDto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("City not found for ID: " + subCityOrDivisionDto.getCityId()));

        subCityOrDivision.setId(subCityOrDivisionDto.getId());
        subCityOrDivision.setName(subCityOrDivisionDto.getName());
        subCityOrDivision.setCity(city);
        subCityOrDivision.setDescription(subCityOrDivisionDto.getDescription());

        SubCityOrDivision updatedSubCityOrDivision = subCityOrDivisionRepository.save(subCityOrDivision);

        log.info("Sub-city or division updated successfully: {}", updatedSubCityOrDivision.getId());
        return SubCityOrDivisionMapper.toDto(updatedSubCityOrDivision);
    }


    /**
     * Adds a new sub-city or division.
     *
     * @param subCityOrDivisionDto the DTO containing the details of the new sub-city or division
     * @return a SubCityOrDivisionDto object containing the details of the newly created sub-city or division
     */
    @Override
    public SubCityOrDivisionDto addNewSubCityOrDivisionByAdmin(SubCityOrDivisionCreateDto subCityOrDivisionDto) {

        log.info("Adding new sub-city or division with name: {}", subCityOrDivisionDto.getName());
        SubCityOrDivision subCityOrDivision = new SubCityOrDivision();
        subCityOrDivision.setName(subCityOrDivisionDto.getName());
        City city = cityRepository.findById(subCityOrDivisionDto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("City not found for ID: " + subCityOrDivisionDto.getCityId()));
        subCityOrDivision.setCity(city);
        subCityOrDivision.setDescription(subCityOrDivisionDto.getDescription());

        SubCityOrDivision savedSubCityOrDivision = subCityOrDivisionRepository.save(subCityOrDivision);

        log.info("New sub-city or division added successfully with ID: {}", savedSubCityOrDivision.getId());
        return SubCityOrDivisionMapper.toDto(savedSubCityOrDivision);
    }

    /**
     * Deletes a sub-city or division by its ID.
     *
     * @param subCityOrDivisionId the ID of the sub-city or division to be deleted
     */
    @Override
    public void deleteSubCityOrDivisionByAdmin(Long subCityOrDivisionId) {
        log.info("Deleting sub-city or division with ID: {}", subCityOrDivisionId);
        SubCityOrDivision subCityOrDivision = subCityOrDivisionRepository.findById(subCityOrDivisionId).orElseThrow(() -> new ResourceNotFoundException("Sub city or division not found for ID: " + subCityOrDivisionId));
        subCityOrDivisionRepository.delete(subCityOrDivision);
        log.info("Sub-city or division deleted successfully with ID: {}", subCityOrDivisionId);

    }

}
