package com.muyategna.backend.location.service;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.repository.LanguageRepository;
import com.muyategna.backend.location.dto.city.CityTranslationCreateDto;
import com.muyategna.backend.location.dto.city.CityTranslationDto;
import com.muyategna.backend.location.dto.city.CityTranslationUpdateDto;
import com.muyategna.backend.location.entity.City;
import com.muyategna.backend.location.entity.CityTranslation;
import com.muyategna.backend.location.mapper.CityTranslationMapper;
import com.muyategna.backend.location.repository.CityRepository;
import com.muyategna.backend.location.repository.CityTranslationRepository;
import com.muyategna.backend.system.exception.DataIntegrityException;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CityTranslationServiceImpl implements CityTranslationService {

    private final CityTranslationRepository cityTranslationRepository;
    private final CityRepository cityRepository;
    private final LanguageRepository languageRepository;

    @Autowired
    public CityTranslationServiceImpl(CityTranslationRepository cityTranslationRepository, CityRepository cityRepository, LanguageRepository languageRepository) {
        this.cityTranslationRepository = cityTranslationRepository;
        this.cityRepository = cityRepository;
        this.languageRepository = languageRepository;
    }

    /**
     * Retrieves a paged list of city translations by city ID.
     *
     * @param cityId   City ID to filter on
     * @param pageable Pageable object containing pagination parameters
     * @return A paged list of city translations
     * @throws ResourceNotFoundException If no city translations found for the city ID
     */
    @Override
    public Page<CityTranslationDto> getCityTranslationsByCityId(Long cityId, Pageable pageable) {

        log.info("Retrieving city translations by city ID: {}", cityId);
        Page<CityTranslationDto> cityTranslationDtoList = cityTranslationRepository.findByCityId(cityId, pageable).map(CityTranslationMapper::toDto);

        if (cityTranslationDtoList.isEmpty()) {
            throw new ResourceNotFoundException("City translations not found for city ID: " + cityId);
        }

        log.info("Retrieved {} city translations by city ID: {}", cityTranslationDtoList.getTotalElements(), cityId);
        return cityTranslationDtoList;
    }


    /**
     * Retrieves a city translation by its ID.
     *
     * @param translationId City translation ID to filter on
     * @return The city translation details as a CityTranslationDto
     * @throws ResourceNotFoundException If city translation not found for the translation ID
     */
    @Override
    public CityTranslationDto getCityTranslationById(Long translationId) {
        log.info("Retrieving city translation by ID: {}", translationId);
        CityTranslationDto cityTranslationDto = CityTranslationMapper
                .toDto(cityTranslationRepository
                        .findById(translationId)
                        .orElseThrow(() -> new ResourceNotFoundException("City translation not found for translation ID: " + translationId)));

        log.info("Retrieved city translation by ID: {}", translationId);
        return cityTranslationDto;
    }

    /**
     * Retrieves a city translation by its city ID and language ID.
     *
     * @param cityId     City ID to filter on
     * @param languageId Language ID to filter on
     * @return The city translation details as a CityTranslationDto
     * @throws ResourceNotFoundException If city translation not found for the city ID and language ID
     */
    @Override
    public CityTranslationDto getCityTranslationByCityIdAndLanguageId(Long cityId, Long languageId) {
        log.info("Retrieving city translation by city ID: {} and language ID: {}", cityId, languageId);
        CityTranslationDto cityTranslationDto = cityTranslationRepository
                .findByCityIdAndLanguageId(cityId, languageId)
                .map(CityTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("City translation not found for city ID: " + cityId + " and language ID: " + languageId));

        log.info("Retrieved city translation by city ID: {} and language ID: {}", cityId, languageId);
        return cityTranslationDto;
    }


    /**
     * Retrieves a city translation by its city ID and locale.
     *
     * @param cityId City ID to filter on
     * @param locale Locale to filter on
     * @return The city translation details as a CityTranslationDto
     * @throws ResourceNotFoundException If city translation not found for the city ID and locale
     */
    @Override
    public CityTranslationDto getCityTranslationByCityIdAndLocale(Long cityId, String locale) {

        log.info("Retrieving city translation by city ID: {} and locale: {}", cityId, locale);
        Language language = languageRepository.findByLocale(locale).orElseThrow(() -> new ResourceNotFoundException("Language not found for locale: " + locale));
        CityTranslationDto cityTranslationDto = cityTranslationRepository
                .findByCityIdAndLanguageId(cityId, language.getId())
                .map(CityTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("City translation not found for city ID: " + cityId + " and locale: " + locale));

        log.info("Retrieved city translation by city ID: {} and locale: {}", cityId, locale);
        return cityTranslationDto;
    }


    /**
     * Updates a city translation with the specified ID.
     *
     * @param translationId      The ID of the city translation to be updated
     * @param cityTranslationDto The city translation object with new values
     * @return The updated city translation details as a CityTranslationDto
     * @throws ResourceNotFoundException If the city or language is not found
     * @throws DataIntegrityException    If the city and language does not match
     */
    @Override
    public CityTranslationDto updateCityTranslation(Long translationId, CityTranslationUpdateDto cityTranslationDto) {
        log.info("Updating city translation with ID: {}", translationId);
        CityTranslation cityTranslation = new CityTranslation();
        cityTranslation.setId(translationId);
        cityTranslation.setName(cityTranslationDto.getName());
        cityTranslation.setDescription(cityTranslationDto.getDescription());

        // Retrieve city and language
        City city = cityRepository.findById(cityTranslationDto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("City not found for city ID: " + cityTranslationDto.getCityId()));
        Language language = languageRepository.findById(cityTranslationDto.getLanguageId()).orElseThrow(() -> new ResourceNotFoundException("Language not found for language ID: " + cityTranslationDto.getLanguageId()));

        if (!city.getRegion().getCountry().getCountryIsoCode2().equals(language.getCountry().getCountryIsoCode2()) && !language.getCountry().isGlobal()) {
            log.error("City and language does not match for city ID: {} and language ID: {}.", cityTranslationDto.getCityId(), cityTranslationDto.getLanguageId());
            throw new DataIntegrityException("City and language does not match for city ID: " + cityTranslationDto.getCityId() + " and language ID: " + cityTranslationDto.getLanguageId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + city.getRegion().getCountry().getCountryIsoCode2());
        }

        cityTranslation.setLanguage(language);
        cityTranslation.setCity(city);

        log.info("Updated city translation with ID: {}", translationId);
        return CityTranslationMapper.toDto(cityTranslationRepository.save(cityTranslation));
    }


    /**
     * Creates a new city translation.
     *
     * @param cityTranslationDto The city translation to be created
     * @return The created city translation details as a CityTranslationDto
     * @throws ResourceNotFoundException If the city or language is not found
     * @throws DataIntegrityException    If the city and language does not match
     */
    @Override
    public CityTranslationDto addNewCityTranslation(CityTranslationCreateDto cityTranslationDto) {
        log.info("Adding new city translation");
        CityTranslation cityTranslation = new CityTranslation();
        cityTranslation.setName(cityTranslationDto.getName());
        cityTranslation.setDescription(cityTranslationDto.getDescription());

        City city = cityRepository.findById(cityTranslationDto.getCityId()).orElseThrow(() -> new ResourceNotFoundException("City not found for city ID: " + cityTranslationDto.getCityId()));
        Language language = languageRepository.findById(cityTranslationDto.getLanguageId()).orElseThrow(() -> new ResourceNotFoundException("Language not found for language ID: " + cityTranslationDto.getLanguageId()));

        if (!city.getRegion().getCountry().getCountryIsoCode2().equals(language.getCountry().getCountryIsoCode2()) && !language.getCountry().isGlobal()) {
            log.error("City and language does not match for city ID: {} and language ID:  {}.", cityTranslationDto.getCityId(), cityTranslationDto.getLanguageId());
            throw new DataIntegrityException("City and language does not match for city ID: " + cityTranslationDto.getCityId() + " and language ID: " + cityTranslationDto.getLanguageId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + city.getRegion().getCountry().getCountryIsoCode2());
        }

        cityTranslation.setLanguage(language);
        cityTranslation.setCity(city);

        log.info("Added new city translation with id: {}", cityTranslation.getId());
        return CityTranslationMapper.toDto(cityTranslationRepository.save(cityTranslation));
    }


    /**
     * Deletes a city translation by its ID.
     *
     * @param translationId City translation ID to delete
     * @throws ResourceNotFoundException If the city translation is not found for the translation ID
     */
    @Override
    public void deleteCityTranslation(Long translationId) {
        log.info("Deleting city translation with ID: {}", translationId);
        CityTranslation cityTranslation = cityTranslationRepository.findById(translationId).orElseThrow(() -> new ResourceNotFoundException("City translation not found for translation ID: " + translationId));
        cityTranslationRepository.delete(cityTranslation);
        log.info("Deleted city translation with ID: {}", translationId);
    }
}
