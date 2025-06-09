package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.CacheEvictionService;
import com.muyategna.backend.location.dto.country.*;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.location.mapper.CountryTranslationMapper;
import com.muyategna.backend.location.repository.CountryRepository;
import com.muyategna.backend.location.repository.CountryTranslationRepository;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryTranslationService countryTranslationService;
    private final CountryTranslationRepository countryTranslationRepository;
    private final CacheEvictionService cacheEvictionService;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository,
                              CountryTranslationService countryTranslationService,
                              CountryTranslationRepository countryTranslationRepository,
                              CacheEvictionService cacheEvictionService) {
        this.countryRepository = countryRepository;
        this.countryTranslationService = countryTranslationService;
        this.countryTranslationRepository = countryTranslationRepository;
        this.cacheEvictionService = cacheEvictionService;
    }


    /**
     * Retrieves the global country from the database.
     *
     * @return the global country as a CountryDto
     * @throws ResourceNotFoundException if the global country is not found
     */
    @Override
    public CountryDto getGlobalCountry() {
        log.info("Retrieving global country");
        return countryRepository.findByIsGlobal(true)
                .map(CountryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Global country not found. Please check the database."));
    }


    /**
     * Retrieves the current user's country in the current language.
     * <p>
     * This method retrieves the current user's country from the database and its translation in the current language.
     * <p>
     * If the country does not exist, or its translation in the current language does not exist,
     * a ResourceNotFoundException will be thrown.
     *
     * @param countryDto  the country to be retrieved
     * @param languageDto the language to be used for translation
     * @return the localized country details as a CountryLocalizedDto
     * @throws ResourceNotFoundException if the country or its translation is not found
     */
    @Override
    public CountryLocalizedDto getCurrentCountryPublic(CountryDto countryDto, LanguageDto languageDto) {
        log.info("Retrieving current user's country in the current language countryId={}, languageId={}", countryDto.getId(), languageDto.getId());
        CountryTranslationDto countryTranslationDto = countryTranslationService.findCountryTranslationByCountryAndLanguagePublic(countryDto, languageDto);
        log.info("Retrieved current user's country in the current language countryId={}, languageId={}", countryDto.getId(), languageDto.getId());
        return CountryMapper.toLocalizedDto(countryDto, countryTranslationDto);
    }


    /**
     * Retrieves a country by its ID.
     * <p>
     * This method retrieves a country from the database by its ID.
     * <p>
     * If the country does not exist, a ResourceNotFoundException will be thrown.
     *
     * @param countryId the id of the country to be retrieved
     * @return the country details as a CountryDto
     * @throws ResourceNotFoundException if the country is not found
     */
    @Override
    public CountryDto getCountryById(Long countryId) {
        log.info("Retrieving country by id: {}", countryId);
        CountryDto countryDto = countryRepository
                .findById(countryId)
                .map(CountryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryId + ". Please check the database."));
        log.info("Retrieved country by id: {}", countryId);
        return countryDto;
    }


    /**
     * Retrieves the localized details of a country using its ISO code.
     *
     * <p>This method fetches a country by its ISO code and retrieves its translation
     * in the current language context. If the country or its translation is not found,
     * a ResourceNotFoundException is thrown.
     *
     * @param countryIsoCode2 the ISO code of the country
     * @return the localized country details as a CountryLocalizedDto
     * @throws ResourceNotFoundException if the country or its translation is not found
     */
    @Override
    public CountryLocalizedDto getLocalizedCountryByIsoCode2(String countryIsoCode2) {
        log.info("Retrieving localized country by ISO code: {}", countryIsoCode2);
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        Country country = countryRepository.findByCountryIsoCode2(countryIsoCode2)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found. Please check the database."));
        CountryTranslationDto countryTranslationDto = countryTranslationRepository
                .findByCountryIdAndLanguageId(country.getId(), languageDto.getId())
                .map(CountryTranslationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country translation not found. Please check the database."));
        CountryLocalizedDto countryLocalizedDto = CountryMapper.toLocalizedDto(com.muyategna.backend.location.mapper.CountryMapper.toDto(country), countryTranslationDto);
        log.info("Retrieved localized country by ISO code: {}", countryIsoCode2);
        return countryLocalizedDto;
    }


    /**
     * Retrieves a country by its ISO code.
     * <p>
     * This method fetches a country by its ISO code from the database.
     * <p>
     * If the country is not found, a ResourceNotFoundException is thrown.
     *
     * @param countryIsoCode2 the ISO code of the country
     * @return the country details as a CountryDto
     * @throws ResourceNotFoundException if the country is not found
     */
    @Override
    public CountryDto getCountryByIsoCode2(String countryIsoCode2) {
        log.info("Retrieving country by ISO code: {}", countryIsoCode2);
        CountryDto countryDto = countryRepository.findByCountryIsoCode2(countryIsoCode2)
                .map(CountryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found. Please check the database."));

        log.info("Retrieved country by ISO code: {}", countryIsoCode2);
        return countryDto;
    }

    /**
     * Saves a new country to the database.
     * <p>
     * This method converts a CountryCreateDto to a Country entity,
     * saves it to the country repository, and returns the saved entity
     * as a CountryDto.
     *
     * @param createDto the data transfer object containing the details of the country to be created
     * @return the saved country details as a CountryDto
     */
    @Override
    public CountryDto saveCountry(CountryCreateDto createDto) {
        log.info("Saving new country");
        Country country = CountryMapper.toEntity(createDto);
        Country savedCountry = countryRepository.save(country);
        log.info("Saved new country with id: {}", savedCountry.getId());
        return CountryMapper.toDto(savedCountry);
    }

    /**
     * Retrieves a list of all countries.
     * <p>
     * This method fetches all country records from the database,
     * converts each to a CountryDto, and returns them as a list.
     *
     * @return a list of CountryDto objects representing all countries
     */
    @Override
    public List<CountryDto> getAllCountries() {
        log.info("Retrieving all countries");
        List<CountryDto> countryDtoList = countryRepository.findAll().stream()
                .map(CountryMapper::toDto)
                .toList();

        log.info("Retrieved all countries");
        return countryDtoList;
    }


    /**
     * Updates a country in the database.
     * <p>
     * This method updates the corresponding fields of the country with the provided id,
     * and returns the updated country details as a CountryDto.
     * <p>
     * If no country is found with the given id, a ResourceNotFoundException is thrown.
     *
     * @param countryId the id of the country to be updated
     * @param updateDto the data transfer object containing the details of the country to be updated
     * @return the updated country details as a CountryDto
     */
    @Override
    public CountryDto updateCountry(Long countryId, CountryUpdateDto updateDto) {
        log.info("Updating country with id: {}", countryId);
        Country country = countryRepository.findById(countryId).orElseThrow(() -> new ResourceNotFoundException("Country not found. Please check the database."));
        country.setName(updateDto.getName());
        country.setCountryIsoCode2(updateDto.getCountryIsoCode2());
        country.setCountryIsoCode3(updateDto.getCountryIsoCode3());
        country.setCountryIsoCodeNumeric(updateDto.getCountryIsoCodeNumeric());
        country.setContinent(updateDto.getContinent());
        country.setDescription(updateDto.getDescription());
        country.setGlobal(updateDto.isGlobal());
        Country updatedCountry = countryRepository.save(country);
        cacheEvictionService.evictCountryCache(country);

        log.info("Updated country with id: {}", countryId);
        return CountryMapper.toDto(updatedCountry);
    }

    /**
     * Deletes a country by its ID.
     * <p>
     * This method removes a country from the database using the provided country ID.
     * If the country does not exist, a ResourceNotFoundException is thrown.
     * It also evicts the country from the cache after deletion.
     *
     * @param countryId the ID of the country to be deleted
     * @throws ResourceNotFoundException if the country is not found
     */
    @Override
    public void deleteCountry(Long countryId) {
        log.info("Deleting country with id: {}", countryId);
        Country country = countryRepository.findById(countryId).orElseThrow(() -> new ResourceNotFoundException("Country not found. Please check the database."));
        countryRepository.delete(country);
        log.info("Deleted country with id: {}", countryId);
        log.info("Evicting country from cache after deletion of country with id: {}", countryId);
        cacheEvictionService.evictCountryCache(country);
        log.info("Evicted country from cache after deletion of country with id: {}", countryId);
    }
}
