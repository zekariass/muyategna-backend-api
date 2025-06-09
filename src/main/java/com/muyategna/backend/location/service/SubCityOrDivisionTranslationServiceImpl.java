package com.muyategna.backend.location.service;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.repository.LanguageRepository;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationCreateDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationUpdateDto;
import com.muyategna.backend.location.entity.SubCityOrDivision;
import com.muyategna.backend.location.entity.SubCityOrDivisionTranslation;
import com.muyategna.backend.location.mapper.SubCityOrDivisionTranslationMapper;
import com.muyategna.backend.location.repository.SubCityOrDivisionRepository;
import com.muyategna.backend.location.repository.SubCityOrDivisionTranslationRepository;
import com.muyategna.backend.system.exception.DataIntegrityException;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubCityOrDivisionTranslationServiceImpl implements SubCityOrDivisionTranslationService {

    private final SubCityOrDivisionTranslationRepository subCityOrDivisionTranslationRepository;
    private final SubCityOrDivisionRepository subCityOrDivisionRepository;
    private final LanguageRepository languageRepository;

    public SubCityOrDivisionTranslationServiceImpl(SubCityOrDivisionTranslationRepository subCityOrDivisionTranslationRepository, SubCityOrDivisionRepository subCityOrDivisionRepository, LanguageRepository languageRepository) {
        this.subCityOrDivisionTranslationRepository = subCityOrDivisionTranslationRepository;
        this.subCityOrDivisionRepository = subCityOrDivisionRepository;
        this.languageRepository = languageRepository;
    }

    /**
     * Retrieves a list of SubCityOrDivisionTranslationDTO objects by the subCityOrDivisionId and languageId.
     *
     * @param subCityOrDivisionId The subCityOrDivisionId
     * @param languageId          The languageId
     * @return A list of SubCityOrDivisionTranslationDTO objects
     */
    @Override
    public SubCityOrDivisionTranslationDto getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLanguageId(Long subCityOrDivisionId, long languageId) {
        log.info("Retrieving SubCityOrDivisionTranslation by subCityOrDivisionId: {} and languageId: {}", subCityOrDivisionId, languageId);
        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto = subCityOrDivisionTranslationRepository
                .findBySubCityOrDivisionIdAndLanguageId(subCityOrDivisionId, languageId)
                .map(SubCityOrDivisionTranslationMapper::toDto)
                .orElse(null);
        log.info("Retrieved SubCityOrDivisionTranslation found: {}", subCityOrDivisionTranslationDto);
        return subCityOrDivisionTranslationDto;
    }

    /**
     * Retrieves a list of SubCityOrDivisionTranslationDTO objects by the subCityOrDivisionIds and languageId.
     *
     * @param subCityOrDivisionIds The list of subCityOrDivisionIds
     * @param languageId           The languageId
     * @return A list of SubCityOrDivisionTranslationDTO objects
     */
    @Override
    public List<SubCityOrDivisionTranslationDto> getSubCityOrDivisionTranslationsBySubCityOrDivisionIdsAndLanguageId(List<Long> subCityOrDivisionIds, long languageId) {
        log.info("Retrieving SubCityOrDivisionTranslations by subCityOrDivisionIds: {} and languageId: {}", subCityOrDivisionIds, languageId);
        List<SubCityOrDivisionTranslation> translations = subCityOrDivisionTranslationRepository
                .findBySubCityOrDivisionIdAndLanguageId(subCityOrDivisionIds, languageId);
        log.info("Retrieved SubCityOrDivision Translations: {}", translations.size());
        return translations.stream().map(SubCityOrDivisionTranslationMapper::toDto).toList();
    }

    /**
     * Retrieves a paginated list of SubCityOrDivisionTranslationDTO objects by the subCityOrDivisionId.
     *
     * @param subCityOrDivisionId The subCityOrDivisionId
     * @param pageable            The pagination information
     * @return A paginated list of SubCityOrDivisionTranslationDTO objects
     */
    @Override
    public Page<SubCityOrDivisionTranslationDto> getSubCityOrDivisionTranslationsBySubCityOrDivisionId(Long subCityOrDivisionId, Pageable pageable) {

        log.info("Retrieving SubCityOrDivisionTranslations by subCityOrDivisionId: {}", subCityOrDivisionId);
        Page<SubCityOrDivisionTranslation> translations = subCityOrDivisionTranslationRepository
                .findBySubCityOrDivisionId(subCityOrDivisionId, pageable);
        log.info("Retrieved SubCityOrDivisionTranslations: {}", translations.getTotalElements());
        return translations.map(SubCityOrDivisionTranslationMapper::toDto);
    }


    /**
     * Retrieves a SubCityOrDivisionTranslationDTO object by the translationId.
     *
     * @param translationId The translationId
     * @return A SubCityOrDivisionTranslationDTO object
     */
    @Override
    public SubCityOrDivisionTranslationDto getSubCityOrDivisionTranslationById(Long translationId) {
        log.info("Retrieving SubCityOrDivisionTranslation by id: {}", translationId);
        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto = subCityOrDivisionTranslationRepository.findById(translationId)
                .map(SubCityOrDivisionTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SubCityOrDivision translation not found with id: " + translationId + ". Please check the database."));

        log.info("Retrieved SubCityOrDivisionTranslation: {}", subCityOrDivisionTranslationDto.getId());
        return subCityOrDivisionTranslationDto;
    }

    /**
     * Retrieves a SubCityOrDivisionTranslationDTO object by the subCityOrDivisionId and locale.
     *
     * @param subCityOrDivisionId The subCityOrDivisionId
     * @param locale              The locale
     * @return A SubCityOrDivisionTranslationDTO object
     */
    @Override
    public SubCityOrDivisionTranslationDto getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLocale(Long subCityOrDivisionId, String locale) {

        log.info("Retrieving SubCityOrDivision Translation by subCityOrDivisionId: {} and locale: {}", subCityOrDivisionId, locale);
        Language language = languageRepository.findByLocale(locale).orElseThrow(() -> new ResourceNotFoundException("Language not found with locale: " + locale + ". Please check the database."));
        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto = subCityOrDivisionTranslationRepository
                .findBySubCityOrDivisionIdAndLanguageId(subCityOrDivisionId, language.getId())
                .map(SubCityOrDivisionTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SubCityOrDivision translation not found with locale: " + locale + ". Please check the database."));
        log.info("Retrieved SubCityOrDivision Translation: {}", subCityOrDivisionTranslationDto.getId());
        return subCityOrDivisionTranslationDto;
    }


    /**
     * Updates a SubCityOrDivisionTranslation object by the translationId and SubCityOrDivisionTranslationUpdateDto.
     *
     * @param translationId                         The translationId
     * @param subCityOrDivisionTranslationUpdateDto The SubCityOrDivisionTranslationUpdateDto
     * @return A SubCityOrDivisionTranslationDTO object
     */
    @Override
    public SubCityOrDivisionTranslationDto updateSubCityOrDivisionTranslation(Long translationId, SubCityOrDivisionTranslationUpdateDto subCityOrDivisionTranslationUpdateDto) {

        log.info("Updating SubCityOrDivisionTranslation with id: {}", translationId);
        SubCityOrDivisionTranslation subCityOrDivisionTranslation = new SubCityOrDivisionTranslation();

        SubCityOrDivision subCityOrDivision = subCityOrDivisionRepository.findById(subCityOrDivisionTranslationUpdateDto.getSubCityOrDivisionId()).orElseThrow(() -> new ResourceNotFoundException("SubCityOrDivision not found with id: " + subCityOrDivisionTranslationUpdateDto.getSubCityOrDivisionId() + ". Please check the database."));
        Language language = languageRepository.findById(subCityOrDivisionTranslationUpdateDto.getLanguageId()).orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + subCityOrDivisionTranslationUpdateDto.getLanguageId() + ". Please check the database."));

        if (!subCityOrDivision.getCity().getRegion().getCountry().getCountryIsoCode2().equals(language.getCountry().getCountryIsoCode2()) && !language.getCountry().isGlobal()) {
            log.error("SubCityOrDivision and language does not match for SubCityOrDivision ID: {} and language ID: {}. Language country ISO code 2: {} does not match city country ISO code 2: {}", subCityOrDivision.getId(), language.getId(), language.getCountry().getCountryIsoCode2(), subCityOrDivision.getCity().getRegion().getCountry().getCountryIsoCode2());
            throw new DataIntegrityException("SubCityOrDivision and language does not match for SubCityOrDivision ID: " + subCityOrDivision.getId() + " and language ID: " + language.getId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + subCityOrDivision.getCity().getRegion().getCountry().getCountryIsoCode2());
        }

        subCityOrDivisionTranslation.setId(translationId);
        subCityOrDivisionTranslation.setSubCityOrDivision(subCityOrDivision);
        subCityOrDivisionTranslation.setLanguage(language);
        subCityOrDivisionTranslation.setName(subCityOrDivisionTranslationUpdateDto.getName());
        subCityOrDivisionTranslation.setDescription(subCityOrDivisionTranslationUpdateDto.getDescription());

        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto = SubCityOrDivisionTranslationMapper.toDto(subCityOrDivisionTranslationRepository.save(subCityOrDivisionTranslation));
        log.info("Updated SubCityOrDivision Translation: {}", subCityOrDivisionTranslationDto.getId());
        return subCityOrDivisionTranslationDto;
    }


    /**
     * Adds a new SubCityOrDivisionTranslation object.
     *
     * @param subCityOrDivisionTranslationCreateDto The SubCityOrDivisionTranslationCreateDto
     * @return A SubCityOrDivisionTranslationDTO object
     */
    @Override
    public SubCityOrDivisionTranslationDto addNewSubCityOrDivisionTranslation(SubCityOrDivisionTranslationCreateDto subCityOrDivisionTranslationCreateDto) {
        log.info("Adding new SubCityOrDivision Translation");
        SubCityOrDivisionTranslation subCityOrDivisionTranslation = new SubCityOrDivisionTranslation();

        SubCityOrDivision subCityOrDivision = subCityOrDivisionRepository.findById(subCityOrDivisionTranslationCreateDto.getSubCityOrDivisionId()).orElseThrow(() -> new ResourceNotFoundException("SubCityOrDivision not found with id: " + subCityOrDivisionTranslationCreateDto.getSubCityOrDivisionId() + ". Please check the database."));
        Language language = languageRepository.findById(subCityOrDivisionTranslationCreateDto.getLanguageId()).orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + subCityOrDivisionTranslationCreateDto.getLanguageId() + ". Please check the database."));

        if (!subCityOrDivision.getCity().getRegion().getCountry().getCountryIsoCode2().equals(language.getCountry().getCountryIsoCode2()) && !language.getCountry().isGlobal()) {
            log.error("SubCityOrDivision and language does not match for SubCityOrDivision ID: {} and language ID: {}. Language country ISO code 2: {} does not match city country ISO  code 2: {}", subCityOrDivision.getId(), language.getId(), language.getCountry().getCountryIsoCode2(), subCityOrDivision.getCity().getRegion().getCountry().getCountryIsoCode2());
            throw new DataIntegrityException("SubCityOrDivision and language does not match for SubCityOrDivision ID: " + subCityOrDivision.getId() + " and language ID: " + language.getId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + subCityOrDivision.getCity().getRegion().getCountry().getCountryIsoCode2());
        }

        subCityOrDivisionTranslation.setSubCityOrDivision(subCityOrDivision);
        subCityOrDivisionTranslation.setLanguage(language);
        subCityOrDivisionTranslation.setName(subCityOrDivisionTranslationCreateDto.getName());
        subCityOrDivisionTranslation.setDescription(subCityOrDivisionTranslationCreateDto.getDescription());

        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto = SubCityOrDivisionTranslationMapper.toDto(subCityOrDivisionTranslationRepository.save(subCityOrDivisionTranslation));
        log.info("Added new SubCityOrDivision Translation: {}", subCityOrDivisionTranslationDto.getId());
        return subCityOrDivisionTranslationDto;
    }


    /**
     * Deletes a SubCityOrDivisionTranslation object by the translationId.
     *
     * @param translationId The translationId
     */
    @Override
    public void deleteSubCityOrDivisionTranslation(Long translationId) {
        log.info("Deleting SubCityOrDivisionTranslation with id: {}", translationId);
        SubCityOrDivisionTranslation subCityOrDivisionTranslation = subCityOrDivisionTranslationRepository.findById(translationId).orElseThrow(() -> new ResourceNotFoundException("SubCityOrDivisionTranslation not found with id: " + translationId + ". Please check the database."));
        subCityOrDivisionTranslationRepository.delete(subCityOrDivisionTranslation);
        log.info("Deleted SubCityOrDivisionTranslation with id: {}", translationId);
    }
}
