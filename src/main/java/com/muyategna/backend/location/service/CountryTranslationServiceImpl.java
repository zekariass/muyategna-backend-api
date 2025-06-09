package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.mapper.LanguageMapper;
import com.muyategna.backend.common.repository.LanguageRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.country.CountryTranslationCreateDto;
import com.muyategna.backend.location.dto.country.CountryTranslationDto;
import com.muyategna.backend.location.dto.country.CountryTranslationUpdateDto;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.CountryTranslation;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.location.mapper.CountryTranslationMapper;
import com.muyategna.backend.location.repository.CountryRepository;
import com.muyategna.backend.location.repository.CountryTranslationRepository;
import com.muyategna.backend.system.exception.DataIntegrityException;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CountryTranslationServiceImpl implements CountryTranslationService {

    private final CountryTranslationRepository countryTranslationRepository;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;

    public CountryTranslationServiceImpl(CountryTranslationRepository countryTranslationRepository, CountryRepository countryRepository, LanguageRepository languageRepository) {
        this.countryTranslationRepository = countryTranslationRepository;
        this.countryRepository = countryRepository;
        this.languageRepository = languageRepository;
    }


    /**
     * Retrieves a country translation by its ID.
     *
     * @param translationId the ID of the country translation to be retrieved
     * @return a ResponseEntity containing an ApiResponse with the requested CountryTranslationDto entity as an EntityModel
     * @throws ResourceNotFoundException if the country translation is not found
     */
    @Override
    public CountryTranslationDto getCountryTranslationById(Long translationId) {
        log.info("Retrieving country translation with id: {}", translationId);
        CountryTranslationDto countryTranslationDto = countryTranslationRepository
                .findById(translationId)
                .map(CountryTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country translation not found. Please check the database."));
        log.info("Retrieved country translation with id: {}", translationId);
        return countryTranslationDto;
    }


    /**
     * Retrieves a list of country translations for a specified country ID.
     *
     * @param countryId the ID of the country whose translations are to be retrieved
     * @return a list of CountryTranslationDto objects representing the translations
     * @throws ResourceNotFoundException if no translations are found for the given country ID
     */
    @Override
    public List<CountryTranslationDto> getCountryTranslationsByCountryId(Long countryId) {
        log.info("Retrieving country translations for country with id: {}", countryId);
        List<CountryTranslationDto> countryTranslationDtoList = countryTranslationRepository
                .findByCountryId(countryId)
                .stream()
                .map(CountryTranslationMapper::toDto)
                .toList();
        log.info("Retrieved country translations for country with id: {}", countryId);
        return countryTranslationDtoList;
    }


    /**
     * Retrieves a country translation by its country ID and language ID.
     *
     * @param countryId  the ID of the country whose translation is to be retrieved
     * @param languageId the ID of the language in which the translation is to be retrieved
     * @return a CountryTranslationDto object representing the translation
     * @throws ResourceNotFoundException if the country translation is not found
     */
    @Override
    public CountryTranslationDto getCountryTranslationByCountryIdAndLanguageId(Long countryId, Long languageId) {
        log.info("Retrieving country translation with country id: {} and language id: {}", countryId, languageId);
        CountryTranslationDto countryTranslationDto = countryTranslationRepository
                .findByCountryIdAndLanguageId(countryId, languageId)
                .map(CountryTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country translation not found with country id: " + countryId + " and language id: " + languageId + ". Please check the database."));
        log.info("Retrieved country translation with country id: {} and language id: {}", countryId, languageId);
        return countryTranslationDto;
    }

    /**
     * Creates a new country translation.
     *
     * @param countryTranslationDto the DTO containing the details of the country translation to be created
     * @return the created country translation details as a CountryTranslationDto
     * @throws ResourceNotFoundException if the country or language is not found
     * @throws DataIntegrityException    if the country and language do not match
     */
    @Override
    public CountryTranslationDto addNewCountryTranslation(CountryTranslationCreateDto countryTranslationDto) {

        log.info("Creating new country translation with name: {}", countryTranslationDto.getName());
        CountryTranslation countryTranslation = new CountryTranslation();
        countryTranslation.setName(countryTranslationDto.getName());
        countryTranslation.setDescription(countryTranslationDto.getDescription());
        countryTranslation.setContinent(countryTranslationDto.getContinent());

        Country country = countryRepository
                .findById(countryTranslationDto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryTranslationDto.getCountryId() + ". Please check the database."));
        Language language = languageRepository
                .findById(countryTranslationDto
                        .getLanguageId())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + countryTranslationDto.getLanguageId() + ". Please check the database."));

        if (!Objects.equals(country.getId(), language.getCountry().getId()) && !language.getCountry().isGlobal()) {
            log.error("Country and language does not match for country ID: {} and language ID:  {}.", country.getId(), language.getId());
            throw new DataIntegrityException("Country and language does not match for country ID: " + country.getId() + " and language ID: " + language.getId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + country.getCountryIsoCode2());
        }

        countryTranslation.setCountry(country);
        countryTranslation.setLanguage(language);
        CountryTranslation savedCountryTranslation = countryTranslationRepository.save(countryTranslation);

        log.info("Created new country translation with id: {}", savedCountryTranslation.getId());
        return CountryTranslationMapper.toDto(savedCountryTranslation);
    }


    /**
     * Updates a country translation with the given details.
     *
     * @param translationId         the ID of the country translation to be updated
     * @param countryTranslationDto the DTO containing the updated details of the country translation
     * @return the updated country translation details as a CountryTranslationDto
     * @throws ResourceNotFoundException if the country or language is not found
     * @throws DataIntegrityException    if the country and language do not match
     */
    @Override
    public CountryTranslationDto updateCountryTranslation(Long translationId, CountryTranslationUpdateDto countryTranslationDto) {

        log.info("Updating country translation with id: {}", translationId);
        CountryTranslation countryTranslation = new CountryTranslation();
        countryTranslation.setId(translationId);
        countryTranslation.setName(countryTranslationDto.getName());
        countryTranslation.setDescription(countryTranslationDto.getDescription());
        countryTranslation.setContinent(countryTranslationDto.getContinent());

        Country country = countryRepository
                .findById(countryTranslationDto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryTranslationDto.getCountryId() + ". Please check the database."));
        Language language = languageRepository
                .findById(countryTranslationDto
                        .getLanguageId())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + countryTranslationDto.getLanguageId() + ". Please check the database."));

        if (!Objects.equals(country.getId(), language.getCountry().getId()) && !language.getCountry().isGlobal()) {
            log.error("Country and language does not match for country with ID: {} and language ID:  {}.", country.getId(), language.getId());
            throw new DataIntegrityException("Country and language does not match for country ID: " + country.getId() + " and language ID: " + language.getId() + ". Language country ISO code 2: " + language.getCountry().getCountryIsoCode2() + " does not match city country ISO code 2: " + country.getCountryIsoCode2());

        }

        countryTranslation.setCountry(country);
        countryTranslation.setLanguage(language);

        CountryTranslation savedCountryTranslation = countryTranslationRepository.save(countryTranslation);

        log.info("Updated country translation with id: {}", savedCountryTranslation.getId());
        return CountryTranslationMapper.toDto(savedCountryTranslation);
    }


    /**
     * Deletes a country translation with the specified ID.
     *
     * @param translationId the ID of the country translation to be deleted
     * @throws ResourceNotFoundException if no country translation is found with the given ID
     */
    @Override
    public void deleteCountryTranslation(Long translationId) {
        log.info("Deleting country translation with id: {}", translationId);
        CountryTranslation countryTranslation = countryTranslationRepository
                .findById(translationId)
                .orElseThrow(() -> new ResourceNotFoundException("CountryTranslation not found with id: " + translationId + ". Please check the database."));
        countryTranslationRepository.delete(countryTranslation);
        log.info("Deleted country translation with id: {}", translationId);
    }

    /**
     * Retrieves a country translation by its country ID and locale.
     *
     * @param countryId the ID of the country whose translation is to be retrieved
     * @param locale    the locale for which the translation is to be retrieved
     * @return a CountryTranslationDto object representing the translation
     * @throws ResourceNotFoundException if the language for the given locale or the country translation is not found
     */
    @Override
    public CountryTranslationDto getCountryTranslationByCountryIdAndLocale(Long countryId, String locale) {

        log.info("Retrieving country translation by country ID: {} and locale: {}", countryId, locale);
        Language languageDto = languageRepository.findByLocale(locale).orElseThrow(() -> new ResourceNotFoundException("Language not found with locale: " + locale + ". Please check the database."));
        CountryTranslationDto countryTranslationDto = countryTranslationRepository
                .findByCountryIdAndLanguageId(countryId, languageDto.getId())
                .map(CountryTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country translation not found. Please check the database."));
        log.info("Retrieved country translation by country ID: {} and locale: {}", countryId, locale);
        return countryTranslationDto;
    }


    /**
     * Retrieves a country translation by its country and language.
     *
     * @param countryDto  the country whose translation is to be retrieved
     * @param languageDto the language for which the translation is to be retrieved
     * @return a CountryTranslationDto object representing the translation
     * @throws ResourceNotFoundException if the country translation is not found
     */
    @Override
    public CountryTranslationDto findCountryTranslationByCountryAndLanguagePublic(CountryDto countryDto, LanguageDto languageDto) {
        log.info("Retrieving country translation by countryId: {} and languageId: {}", countryDto.getId(), languageDto.getId());
        CountryTranslation countryTranslation = countryTranslationRepository
                .findByCountryAndLanguage(CountryMapper.toEntity(countryDto), LanguageMapper.toEntity(languageDto, countryDto))
                .orElseThrow(() -> new ResourceNotFoundException("CountryTranslation not found"));

        log.info("Retrieved country translation by countryId: {} and languageId: {}", countryDto.getId(), languageDto.getId());
        return CountryTranslationMapper.toDto(countryTranslation);
    }

}
