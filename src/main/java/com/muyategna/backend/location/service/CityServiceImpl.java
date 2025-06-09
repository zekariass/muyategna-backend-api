package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.city.CityCreateDto;
import com.muyategna.backend.location.dto.city.CityDto;
import com.muyategna.backend.location.dto.city.CityLocalizedDto;
import com.muyategna.backend.location.dto.city.CityUpdateDto;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.RegionDto;
import com.muyategna.backend.location.entity.City;
import com.muyategna.backend.location.entity.CityTranslation;
import com.muyategna.backend.location.mapper.CityMapper;
import com.muyategna.backend.location.mapper.RegionMapper;
import com.muyategna.backend.location.repository.CityRepository;
import com.muyategna.backend.location.repository.CityTranslationRepository;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityTranslationRepository cityTranslationRepository;
    private final LanguageService languageService;
    private final RegionService regionService;
    private final CountryService countryService;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CityTranslationRepository cityTranslationRepository, LanguageService languageService, RegionService regionService, CountryService countryService) {
        this.cityRepository = cityRepository;
        this.cityTranslationRepository = cityTranslationRepository;
        this.languageService = languageService;
        this.regionService = regionService;
        this.countryService = countryService;
    }

    /**
     * Retrieves a localized city by its ID and language.
     *
     * @param cityId      the ID of the city to be retrieved
     * @param languageDto the language details for localization
     * @return the localized city details as a CityLocalizedDto
     * @throws ResourceNotFoundException if the city or its translation is not found
     */
    @Override
    public CityLocalizedDto getCityById(Long cityId, LanguageDto languageDto) {
        log.info("Getting city with id: {}", cityId);
        City city = cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));

        if (!city.getRegion().getCountry().getCountryIsoCode2().equals(CountryContextHolder.getCountry().getCountryIsoCode2())) {
            throw new ResourceNotFoundException("City not found with id: " + cityId + " for country with name: " + CountryContextHolder.getCountry().getName());
        }

        CityTranslation cityTranslation = cityTranslationRepository.findByCityIdAndLanguageId(cityId, languageDto.getId()).orElse(null);

        if (cityTranslation == null) {
            LanguageDto globalLanguage = languageService.getGlobalLanguage();
            cityTranslation = cityTranslationRepository.findByCityIdAndLanguageId(globalLanguage.getId(), cityId).orElseThrow(() -> new ResourceNotFoundException("City translation not found for city with id: " + cityId));
        }

        log.info("Retrieved city with id: {}", cityId);

        return CityMapper.toLocalizedDto(city, cityTranslation);
    }


    /**
     * Retrieves a list of cities by region ID and language ID, with fallback to global language if translation not found.
     *
     * @param regionId   the ID of the region
     * @param languageId the ID of the language
     * @return a list of localized city details as a CityLocalizedDto
     * @throws ResourceNotFoundException if no cities found for the region and language
     */
    @Override
    public List<CityLocalizedDto> getAllCitiesByRegion(Long regionId, long languageId) {
        log.info("Retrieving cities for regionId: {}, languageId: {}", regionId, languageId); // logg
        List<City> cities = cityRepository.findByRegionId(regionId);
        List<Long> cityIds = cities.stream().map(City::getId).toList();
        List<CityTranslation> cityTranslations = cityTranslationRepository.findByLanguageIdAndCityIdIn(languageId, cityIds);

        /* Create a map of cityId to CityTranslation */
        Map<Long, CityTranslation> cityTranslationMap = cityTranslations
                .stream()
                .collect(Collectors.toMap(tr -> tr.getCity().getId(), Function.identity()));

        /* Fallback to global language  if translation not found */
        List<Long> fallbackLanguageIds = new ArrayList<>();
        for (Long cityId : cityIds) {
            if (cityTranslationMap.get(cityId) == null) {
                fallbackLanguageIds.add(cityId);
            }
        }

        if (!fallbackLanguageIds.isEmpty()) {
            log.info("Fallback to global language for {} cities", fallbackLanguageIds.size());
            LanguageDto globalLanguage = languageService.getGlobalLanguage();
            List<CityTranslation> fallbackTranslations = cityTranslationRepository
                    .findByLanguageIdAndCityIdIn(globalLanguage.getId(), fallbackLanguageIds);

            cityTranslations = new ArrayList<>(cityTranslations.stream().filter(Objects::nonNull).toList());
            cityTranslations.addAll(fallbackTranslations);
        }

        List<CityLocalizedDto> cityLocalizedDtoList = cityTranslations.stream().map(tr -> CityMapper.toLocalizedDto(tr.getCity(), tr)).toList();

        if (cityLocalizedDtoList.isEmpty()) {
            throw new ResourceNotFoundException("No cities found for region with id: " + regionId + " and language with id: " + languageId);
        }

        log.info("Retrieved {} cities for regionId: {}, languageId: {}", cityLocalizedDtoList.size(), regionId, languageId);
        return cityLocalizedDtoList;
    }

    /**
     * Retrieves a paginated list of cities by region ID for admin.
     *
     * @param regionId the ID of the region
     * @param pageable the pagination parameters
     * @return a paginated list of city details as a CityDto
     * @throws ResourceNotFoundException if no cities found for the region
     */
    @Override
    public Page<CityDto> getAllCitiesByRegionForAdmin(Long regionId, Pageable pageable) {
        log.info("Retrieving cities for regionId: {}", regionId);
        Page<CityDto> cities = cityRepository.findByRegionId(regionId, pageable).map(CityMapper::toDto);

        if (cities.isEmpty()) {
            throw new ResourceNotFoundException("No cities found for region with id: " + regionId);
        }

        log.info("Retrieved {} cities for regionId: {}", cities.getTotalElements(), regionId);
        return cities;
    }


    /**
     * Retrieves a city by its ID for admin.
     *
     * @param cityId the ID of the city to be retrieved
     * @return the city details as a CityDto
     * @throws ResourceNotFoundException if the city is not found
     */
    @Override
    public CityDto getCityByIdForAdmin(Long cityId) {
        log.info("Retrieving city with id: {}", cityId);
        CityDto cityDto = cityRepository.findById(cityId).map(CityMapper::toDto).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));
        log.info("Retrieved city for id: {}", cityId);
        return cityDto;
    }

    /**
     * Retrieves a paginated list of all cities for admin.
     *
     * @param pageable the pagination parameters
     * @return a paginated list of city details as a CityDto
     * @throws ResourceNotFoundException if no cities found in database
     */
    @Override
    public Page<CityDto> getAllCitiesForAdmin(Pageable pageable) {
        log.info("Retrieving all cities by admin");
        Page<City> cities = cityRepository.findAll(pageable);
        if (cities.isEmpty()) {
            log.error("No cities found in database");
            throw new ResourceNotFoundException("No cities found in database");
        }

        log.info("Retrieved {} cities by admin", cities.getTotalElements());
        return cities.map(CityMapper::toDto);
    }

    /**
     * Adds a new city to the system based on the provided city creation details.
     *
     * @param cityCreateDto the data transfer object containing details of the city to be created
     * @return the created city details as a CityDto
     * @throws ResourceNotFoundException if the region or country specified in the cityCreateDto is not found
     */
    @Override
    public CityDto addNewCityByAdmin(CityCreateDto cityCreateDto) {
        log.info("Adding new city by admin");
        RegionDto regionDto = regionService.getRegionById(cityCreateDto.getRegionId());
        CountryDto countryDto = countryService.getCountryById(regionDto.getCountryId());
        City city = CityMapper.toEntity(cityCreateDto, regionDto, countryDto);
        City savedCity = cityRepository.save(city);
        log.info("Added new city with id: {}", savedCity.getId());
        return CityMapper.toDto(savedCity);
    }

    /**
     * Updates an existing city based on the provided city ID and update details.
     *
     * @param cityId        the ID of the city to be updated
     * @param cityUpdateDto the data transfer object containing updated city details
     * @return the updated city details as a CityDto
     * @throws ResourceNotFoundException if the city, region, or country is not found
     */
    @Override
    public CityDto updateCityByAdmin(Long cityId, CityUpdateDto cityUpdateDto) {

        log.info("Updating city with id: {}", cityId);
        City city = cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));
        city.setName(cityUpdateDto.getName());
        city.setDescription(cityUpdateDto.getDescription());

        RegionDto regionDto = regionService.getRegionById(cityUpdateDto.getRegionId());
        CountryDto countryDto = countryService.getCountryById(regionDto.getCountryId());
        city.setRegion(RegionMapper.toEntity(regionDto, countryDto));
        City savedCity = cityRepository.save(city);

        log.info("Updated city with id: {}", savedCity.getId());
        return CityMapper.toDto(savedCity);
    }

    /**
     * Deletes a city based on the provided city ID.
     *
     * @param cityId the ID of the city to be deleted
     * @throws ResourceNotFoundException if the city is not found
     */
    @Override
    public void deleteCityByAdmin(Long cityId) {
        log.info("Deleting city with id: {}", cityId);
        City city = cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + cityId));
        log.info("Deleted city with id: {}", cityId);
        cityRepository.delete(city);
    }

    /**
     * Retrieves a city by ID.
     *
     * @param cityId the ID of the city to be retrieved
     * @return the city details as a CityDto, or null if no city is found with the given ID
     */
    @Override
    public CityDto getCityById(Long cityId) {
        log.info("Retrieving city for id: {}", cityId);
        CityDto cityDto = cityRepository.findById(cityId).map(CityMapper::toDto).orElse(null);
        log.info("Retrieved city for id : {}", cityId);
        return cityDto;
    }

}
