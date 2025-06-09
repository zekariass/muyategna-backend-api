package com.muyategna.backend.common.service;

import com.muyategna.backend.common.CommonCacheKeyUtil;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.common.dto.language.LanguageCreateDto;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.dto.language.LanguageUpdateDto;
import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.mapper.LanguageMapper;
import com.muyategna.backend.common.repository.LanguageRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.location.service.CountryService;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final CacheEvictionService cacheEvictionService;
    private final CacheManager cacheManager;
    private final CountryService countryService;

    public LanguageServiceImpl(LanguageRepository languageRepository, CacheEvictionService cacheEvictionService, CacheManager cacheManager, CountryService countryService) {
        this.languageRepository = languageRepository;
        this.cacheEvictionService = cacheEvictionService;
        this.cacheManager = cacheManager;
        this.countryService = countryService;
    }


    /**
     * Retrieves a language by its ID.
     *
     * @param id the ID of the language to retrieve
     * @return the Language object if found
     * @throws RuntimeException if no language is found with the given ID
     */
    @Cacheable(value = Constants.CACHE_COMMON,
            key = "T(com.muyategna.backend.common.CommonCacheKeyUtil).languageByIdKey(#id)")
    @Transactional(readOnly = true)
    @Override
    public LanguageDto getLanguageById(Long id) {
        log.info("Retrieving language by ID: {}", id);

        // First check if the language is in the cache of all languages
        Cache allLanguagesCache = cacheManager.getCache(Constants.CACHE_COMMON);
        if (allLanguagesCache != null) {
            List<Language> allLanguages = (List<Language>) allLanguagesCache.get(CommonCacheKeyUtil.allLanguagesKey());
            if (allLanguages != null && !allLanguages.isEmpty()) {
                for (Language language : allLanguages) {
                    if (id.equals(language.getId())) {
                        return LanguageMapper.toDto(language);
                    }
                }
            }
        }

        LanguageDto language = languageRepository
                .findById(id)
                .map(LanguageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
        log.info("Language retrieved  successfully: {}", language.getName());
        return language;
    }


    /**
     * Retrieves the default language for a given country for a non login user.
     *
     * @param countryDto is the country object
     * @return an Optional containing the default Language if found, otherwise empty
     */
    @Cacheable(value = Constants.CACHE_COMMON,
            key = "T(com.muyategna.backend.common.CommonCacheKeyUtil).defaultLanguageForCountryKey(#country.id)")
    @Transactional(readOnly = true)
    @Override
    public Optional<LanguageDto> getDefaultLanguageForCountry(CountryDto countryDto) {

        log.info("Retrieving default language for country: {}", countryDto.getName());
        Optional<Language> language = languageRepository.findByCountryAndIsDefault(CountryMapper.toEntity(countryDto), true); //.map(languageMapper::toDto);
        if (language.isEmpty()) {
            log.warn("Default language not found for country: {}. Attempting to retrieve global default language.", countryDto.getName());
            CountryDto globalCountry = countryService.getGlobalCountry();
            language = languageRepository.findByCountryAndIsDefault(CountryMapper.toEntity(globalCountry), true);
        }

        if (language.isEmpty()) {
            log.error("Default language not found for country: {}", countryDto.getName());
            throw new ResourceNotFoundException("Default language not found for country ID: " + countryDto.getId());
        }

        log.info("Default language for country {} retrieved successfully: {}", countryDto.getName(), language.get().getName());
        return language.map(LanguageMapper::toDto);
    }


    /**
     * Retrieves all languages associated with a given country.
     * <p>
     * This method will return all languages associated with the given country.
     * The languages will include both country specific languages and the global language.
     *
     * @param countryDto the country object
     * @return a list of languages associated with the given country
     */
    @Cacheable(value = Constants.CACHE_COMMON,
            key = "T(com.muyategna.backend.common.CommonCacheKeyUtil).languagesByCountryKey(#countryDto.id)")
    @Transactional(readOnly = true)
    @Override
    public List<LanguageDto> getAllLanguagesByCountryPublic(CountryDto countryDto) {
        log.info("Retrieving all languages for country: {}", countryDto.getName());
        List<LanguageDto> languages = new ArrayList<>(languageRepository.findByCountry(CountryMapper.toEntity(countryDto)).stream().map(LanguageMapper::toDto).toList());
        languages.add(getGlobalLanguage());

        log.info("All languages for country {} retrieved successfully", countryDto.getName());
        return languages;
    }

    /**
     * Retrieves a language by its code.
     * This method will return the language object associated with the given code.
     * If the language is not found, the method will return null.
     *
     * @param locale the language code
     * @return a Language object associated with the given code, or null if not found
     */
    @Cacheable(value = Constants.CACHE_COMMON,
            key = "T(com.muyategna.backend.common.CommonCacheKeyUtil).languageByLocaleKey(#locale)")
    @Transactional(readOnly = true)
    @Override
    public LanguageDto getLanguageByLocale(String locale) {
        log.info("Retrieving language by locale: {}", locale);
        LanguageDto language = languageRepository
                .findByLocale(locale)
                .map(LanguageMapper::toDto)
                .orElse(null);
        log.info("Language retrieved successfully: {}", language != null ? language.getName() : "null");
        return language;
    }

    /**
     * Retrieves the global language.
     * This method will return the global language object.
     * If the global language is not found, the method will throw a ResourceNotFoundException.
     *
     * @return a LanguageDto object representing the global language
     */
    @Override
    public LanguageDto getGlobalLanguage() {
        log.info("Retrieving global language from the database");
        LanguageDto globalLanguage = languageRepository
                .findByIsGlobal(true)
                .map(LanguageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Global language not found. Please check the database."));
        log.info("Global language retrieved successfully: {}", globalLanguage.getName());
        return globalLanguage;
    }


    /**
     * Retrieves all languages for admin purposes.
     * This method will return a list of all languages available in the system.
     *
     * @return a list of LanguageDto objects representing all languages
     */
    @Override
    public List<LanguageDto> getAllLanguagesForAdmin() {
        log.info("Retrieving all languages from the database");
        List<LanguageDto> languages = languageRepository
                .findAll()
                .stream()
                .map(LanguageMapper::toDto)
                .toList();
        log.info("All languages retrieved successfully");
        return languages;
    }

    /**
     * Creates a new language and saves it to the database.
     * This method will add a new language and return the saved language object.
     *
     * @param languageCreateDto the language object to be created
     * @return a LanguageDto object representing the saved language
     */
    @Override
    public LanguageDto addNewLanguage(LanguageCreateDto languageCreateDto) {
        log.info("Adding new language: {}", languageCreateDto.getName());
        CountryDto countryDto = countryService.getCountryById(languageCreateDto.getCountryId());
        Language language = LanguageMapper.toEntity(languageCreateDto, countryDto);
        Language savedLanguage = languageRepository.save(language);

        log.info("New language added successfully: {}", savedLanguage.getName());
        return LanguageMapper.toDto(savedLanguage);
    }


    /**
     * Updates an existing language in the database.
     * This method will update the language with the given ID and return the updated language object.
     *
     * @param languageId        the ID of the language to be updated
     * @param languageCreateDto the updated language object
     * @return a LanguageDto object representing the updated language
     */
    @Override
    public LanguageDto updateLanguage(Long languageId, LanguageUpdateDto languageCreateDto) {
        log.info("Updating language with ID: {}", languageId);
        Language language = languageRepository.findById(languageId).orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + languageId));
        CountryDto countryDto = countryService.getCountryById(languageCreateDto.getCountryId());
        language.setName(languageCreateDto.getName());
        language.setLocale(languageCreateDto.getLocale());
        language.setCountry(CountryMapper.toEntity(countryDto));
        language.setNativeName(languageCreateDto.getNativeName());
        language.setDirection(languageCreateDto.getDirection());
        language.setFlagEmoji(languageCreateDto.getFlagEmoji());
        language.setGlobal(languageCreateDto.isGlobal());
        language.setActive(languageCreateDto.isActive());
        language.setDefault(languageCreateDto.isDefault());

        Language updatedLanguage = languageRepository.save(language);
        log.info("Language with ID: {} updated successfully", languageId);
        return LanguageMapper.toDto(updatedLanguage);
    }


    /**
     * Deletes a language by its ID.
     * This method will remove the language from the database.
     *
     * @param languageId the ID of the language to be deleted
     * @throws ResourceNotFoundException if no language is found with the given ID
     */
    @Override
    public void deleteLanguage(Long languageId) {
        log.info("Deleting language with ID: {}", languageId);
        Language language = languageRepository.findById(languageId).orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + languageId));
        languageRepository.delete(language);
        log.info("Language with ID: {} deleted successfully", languageId);
    }

    /**
     * Retrieves all languages associated with a given country ID.
     * This method will return a list of languages for the specified country ID.
     *
     * @param countryId the ID of the country
     * @return a list of LanguageDto objects representing the languages for the specified country
     * @throws ResourceNotFoundException if no languages are found for the given country ID
     */
    @Override
    public List<LanguageDto> getLanguagesByCountryId(Long countryId) {
        log.info("Retrieving languages for country with ID: {}", countryId);
        List<LanguageDto> languages = languageRepository.findByCountryId(countryId).stream().map(LanguageMapper::toDto).toList();
        if (languages.isEmpty()) {
            log.error("No languages found for country with ID: {}", countryId);
            throw new ResourceNotFoundException("No languages found for country with id: " + countryId);
        }

        log.info("Languages for country with ID: {} retrieved successfully", countryId);
        return languages;
    }


}
