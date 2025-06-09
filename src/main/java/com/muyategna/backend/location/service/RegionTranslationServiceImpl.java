package com.muyategna.backend.location.service;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.repository.LanguageRepository;
import com.muyategna.backend.location.dto.region.RegionTranslationCreateDto;
import com.muyategna.backend.location.dto.region.RegionTranslationDto;
import com.muyategna.backend.location.dto.region.RegionTranslationUpdateDto;
import com.muyategna.backend.location.entity.Region;
import com.muyategna.backend.location.entity.RegionTranslation;
import com.muyategna.backend.location.mapper.RegionTranslationMapper;
import com.muyategna.backend.location.repository.RegionRepository;
import com.muyategna.backend.location.repository.RegionTranslationRepository;
import com.muyategna.backend.system.exception.DataIntegrityException;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RegionTranslationServiceImpl implements RegionTranslationService {

    private final RegionTranslationRepository regionTranslationRepository;
    private final RegionRepository regionRepository;
    private final LanguageRepository languageRepository;

    public RegionTranslationServiceImpl(RegionTranslationRepository regionTranslationRepository, RegionRepository regionRepository, LanguageRepository languageRepository) {
        this.regionTranslationRepository = regionTranslationRepository;
        this.regionRepository = regionRepository;
        this.languageRepository = languageRepository;
    }

    /**
     * Retrieves a region translation by its ID.
     *
     * @param translationId the ID of the region translation to be retrieved
     * @return the RegionTranslationDto object representing the region translation with the given ID
     * @throws ResourceNotFoundException if no region translation is found with the given ID
     */
    @Override
    public RegionTranslationDto getRegionTranslationById(Long translationId) {
        log.info("Retrieving region translation with ID: {}", translationId);
        RegionTranslationDto regionTranslationDto = regionTranslationRepository
                .findById(translationId)
                .map(RegionTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Region translation not found with id: " + translationId + ". Please check the database."));
        log.info("Retrieved region translation with ID: {}", translationId);
        return regionTranslationDto;
    }


    /**
     * Retrieves all region translations for a given region ID.
     *
     * @param regionId the ID of the region for which translations are to be retrieved
     * @return a list of RegionTranslationDto objects representing the translations for the region
     * @throws ResourceNotFoundException if no region translations are found for the given region ID
     */
    @Override
    public List<RegionTranslationDto> getRegionTranslationsByRegionId(Long regionId) {

        log.info("Retrieving region translations for region ID: {}", regionId);
        List<RegionTranslationDto> regionTranslationDto = regionTranslationRepository
                .findByRegionId(regionId)
                .stream()
                .map(RegionTranslationMapper::toDto).toList();
        if (regionTranslationDto.isEmpty()) {
            log.error("Region translations not found for region ID: {}", regionId);
            throw new ResourceNotFoundException("Region translations not found for region ID: " + regionId);
        }
        log.info("Retrieved region translations for region ID: {}", regionId);
        return regionTranslationDto;
    }


    /**
     * Retrieves a region translation by its region ID and language ID.
     *
     * @param regionId   the ID of the region for which the translation is to be retrieved
     * @param languageId the ID of the language for which the translation is to be retrieved
     * @return the RegionTranslationDto object representing the translation for the region and language
     * @throws ResourceNotFoundException if no region translation is found for the given region ID and language ID
     */
    @Override
    public RegionTranslationDto getRegionTranslationByRegionIdAndLanguageId(Long regionId, Long languageId) {
        log.info("Retrieving region translation for region ID: {} and language ID: {}", regionId, languageId);
        RegionTranslationDto regionTranslationDto = regionTranslationRepository
                .findByRegionIdAndLanguageId(regionId, languageId)
                .map(RegionTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Region translation not found for region ID: " + regionId + " and language ID: " + languageId));
        log.info("Retrieved region translation for region ID: {} and language ID: {}", regionId, languageId);
        return regionTranslationDto;
    }

    /**
     * Retrieves a region translation by its region ID and locale.
     *
     * @param regionId the ID of the region for which the translation is to be retrieved
     * @param locale   the locale for which the translation is to be retrieved
     * @return the RegionTranslationDto object representing the translation for the region and locale
     * @throws ResourceNotFoundException if no region translation is found for the given region ID and locale
     */
    @Override
    public RegionTranslationDto getRegionTranslationByRegionIdAndLocale(Long regionId, String locale) {
        log.info("Retrieving region translation for region ID: {} and locale: {}", regionId, locale);
        RegionTranslationDto regionTranslationDto = regionTranslationRepository
                .findByRegionIdAndLocale(regionId, locale)
                .map(RegionTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Region translation not found for region ID: " + regionId + " and locale: " + locale));
        log.info("Retrieved region translation for region ID: {} and locale: {}", regionId, locale);
        return regionTranslationDto;
    }


    /**
     * Adds a new region translation.
     *
     * @param regionTranslationDto the RegionTranslationCreateDto object containing the details of the translation to be added
     * @return the RegionTranslationDto object representing the newly added translation
     * @throws ResourceNotFoundException if the region or language specified in the DTO does not exist
     * @throws DataIntegrityException    if the region and language do not match
     */
    @Override
    public RegionTranslationDto addNewRegionTranslation(RegionTranslationCreateDto regionTranslationDto) {

        log.info("Adding new region translation for region ID: {} and language ID: {}", regionTranslationDto.getRegionId(), regionTranslationDto.getLanguageId());
        RegionTranslation regionTranslation = new RegionTranslation();

        Region region = regionRepository
                .findById(regionTranslationDto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + regionTranslationDto.getRegionId() + ". Please check the database."));
        Language language = languageRepository
                .findById(regionTranslationDto.getLanguageId())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + regionTranslationDto.getLanguageId() + ". Please check the database."));

        if (!region.getCountry().getCountryIsoCode2().equals(language.getCountry().getCountryIsoCode2()) && !language.getCountry().isGlobal()) {
            log.error("Region and language do not match for region ID: {} and language ID: {}. Language country ISO code 2: {} does not match region country ISO code 2: {}", regionTranslationDto.getRegionId(), regionTranslationDto.getLanguageId(), language.getCountry().getCountryIsoCode2(), region.getCountry().getCountryIsoCode2());
            throw new DataIntegrityException("Region and language does not match for city ID: " + regionTranslationDto.getRegionId() + " and language ID: " + regionTranslationDto.getLanguageId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + region.getCountry().getCountryIsoCode2());
        }

        regionTranslation.setRegion(region);
        regionTranslation.setLanguage(language);

        regionTranslation.setName(regionTranslationDto.getName());
        regionTranslation.setDescription(regionTranslationDto.getDescription());

        RegionTranslation savedRegionTranslation = regionTranslationRepository.save(regionTranslation);

        log.info("New region translation added with ID: {}", savedRegionTranslation.getId());
        return RegionTranslationMapper.toDto(savedRegionTranslation);
    }


    /**
     * Updates an existing region translation.
     *
     * @param translationId        the ID of the translation to be updated
     * @param regionTranslationDto the RegionTranslationUpdateDto object containing the updated details of the translation
     * @return the RegionTranslationDto object representing the updated translation
     * @throws ResourceNotFoundException if the translation, region, or language specified in the DTO does not exist
     * @throws DataIntegrityException    if the region and language do not match
     */
    @Override
    public RegionTranslationDto updateRegionTranslation(Long translationId, RegionTranslationUpdateDto regionTranslationDto) {

        log.info("Updating region translation with ID: {}", translationId);
        RegionTranslation regionTranslation = regionTranslationRepository
                .findById(translationId)
                .orElseThrow(() -> new ResourceNotFoundException("Region translation not found with id: " + translationId + ". Please check the database."));

        regionTranslation.setId(regionTranslationDto.getId());

        Region region = regionRepository
                .findById(regionTranslationDto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + regionTranslationDto.getRegionId() + ". Please check the database."));

        Language language = languageRepository
                .findById(regionTranslationDto.getLanguageId())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + regionTranslationDto.getLanguageId() + ". Please check the database."));

        if (!region.getCountry().getCountryIsoCode2().equals(language.getCountry().getCountryIsoCode2()) && !language.getCountry().isGlobal()) {
            log.error("Region and language do not match for region ID: {} and language ID: {}. Language  country ISO code 2: {} does not match region country ISO code 2: {}", regionTranslationDto.getRegionId(), regionTranslationDto.getLanguageId(), language.getCountry().getCountryIsoCode2(), region.getCountry().getCountryIsoCode2());
            throw new DataIntegrityException("Region and language does not match for city ID: " + regionTranslationDto.getRegionId() + " and language ID: " + regionTranslationDto.getLanguageId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + region.getCountry().getCountryIsoCode2());
        }

        regionTranslation.setRegion(region);
        regionTranslation.setLanguage(language);

        regionTranslation.setName(regionTranslationDto.getName());
        regionTranslation.setDescription(regionTranslationDto.getDescription());

        RegionTranslation updatedRegionTranslation = regionTranslationRepository.save(regionTranslation);

        log.info("Region translation updated with ID: {}", updatedRegionTranslation.getId());
        return RegionTranslationMapper.toDto(updatedRegionTranslation);
    }


    /**
     * Deletes a region translation by its ID.
     *
     * @param translationId the ID of the translation to be deleted
     * @throws ResourceNotFoundException if no region translation is found with the given ID
     */
    @Override
    public void deleteRegionTranslation(Long translationId) {
        log.info("Deleting region translation with ID: {}", translationId);
        RegionTranslation regionTranslation = regionTranslationRepository
                .findById(translationId)
                .orElseThrow(() -> new ResourceNotFoundException("Region translation not found with id: " + translationId + ". Please check the database."));
        regionTranslationRepository.delete(regionTranslation);
        log.info("Region translation deleted with ID: {}", translationId);
    }
}
