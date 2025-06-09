package com.muyategna.backend.location.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.CacheEvictionService;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.dto.region.RegionCreateDto;
import com.muyategna.backend.location.dto.region.RegionDto;
import com.muyategna.backend.location.dto.region.RegionLocalizedDto;
import com.muyategna.backend.location.dto.region.RegionUpdateDto;
import com.muyategna.backend.location.entity.Region;
import com.muyategna.backend.location.entity.RegionTranslation;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.location.mapper.RegionMapper;
import com.muyategna.backend.location.repository.RegionRepository;
import com.muyategna.backend.location.repository.RegionTranslationRepository;
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
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionTranslationRepository regionTranslationRepository;
    private final CountryService countryService;
    private final CacheEvictionService cacheEvictionService;
    private final LanguageService languageService;

    @Autowired
    public RegionServiceImpl(RegionRepository regionRepository, RegionTranslationRepository regionTranslationRepository, CountryService countryService, CacheEvictionService cacheEvictionService, LanguageService languageService) {
        this.regionRepository = regionRepository;
        this.regionTranslationRepository = regionTranslationRepository;
        this.countryService = countryService;
        this.cacheEvictionService = cacheEvictionService;
        this.languageService = languageService;
    }


    /**
     * Retrieves a list of regions for a specified country and language, with fallback to global language if translation not found.
     *
     * @param countryDto  the country details for which the regions are to be retrieved
     * @param languageDto the language details for localization
     * @return a list of localized region details as a RegionLocalizedDto
     * @throws ResourceNotFoundException if no regions found for the given country and language
     */
    @Override
    public List<RegionLocalizedDto> getAllRegionsByCountryAndLanguage(CountryDto countryDto, LanguageDto languageDto) {
        log.info("Retrieving regions for country: {} and language: {}", countryDto.getName(), languageDto.getName());
        List<Region> regions = regionRepository.findByCountry(CountryMapper.toEntity(countryDto));
        List<Long> regionIds = regions.stream().map(Region::getId).toList();
        List<RegionTranslation> regionTranslations = regionTranslationRepository.findAllByRegionIdsAndLanguageId(regionIds, languageDto.getId());
        Map<Long, RegionTranslation> regionTranslationsMap = regionTranslations.stream().collect(Collectors.toMap(rt -> rt.getRegion().getId(), Function.identity()));

        List<Long> fallBackRegionIds = new ArrayList<>();
        for (Long regionId : regionIds) {
            // Check if translation not found. If so, add to fallback list
            if (regionTranslationsMap.get(regionId) == null) {
                fallBackRegionIds.add(regionId);
            }
        }

        if (!fallBackRegionIds.isEmpty()) {
            // Fallback to global language
            List<RegionTranslation> fallbackRegionTranslations = regionTranslationRepository
                    .findAllByRegionIdsAndLanguageId(fallBackRegionIds, languageService.getGlobalLanguage().getId());

            regionTranslations = new ArrayList<>(regionTranslations.stream().filter(Objects::nonNull).toList());
            regionTranslations.addAll(fallbackRegionTranslations);
        }

        List<RegionLocalizedDto> regionLocalizedDtoList = regions.stream().map(region -> RegionMapper.toLocalizedDto(region, regionTranslationsMap.get(region.getId()))).toList();

        if (regionLocalizedDtoList.isEmpty()) {
            log.error("No regions found for country: {} and language: {}", countryDto.getName(), languageDto.getName());
            throw new ResourceNotFoundException("No regions found for country with name: " + countryDto.getName() + " and language with name: " + languageDto.getName());
        }

        log.info("Retrieved {} regions for country: {} and language: {}", regionLocalizedDtoList.size(), countryDto.getName(), languageDto.getName());
        return regionLocalizedDtoList;
    }


    /**
     * Updates a region with the given id.
     *
     * @param regionId        the id of the region to be updated
     * @param regionUpdateDto the region object to be updated
     * @return the updated region as a RegionDto
     * @throws ResourceNotFoundException if the region is not found
     */
    @Override
    public RegionDto updateRegion(Long regionId, RegionUpdateDto regionUpdateDto) {
        log.info("Updating region with id: {}", regionId);
        Region region = regionRepository.findById(regionId).orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + regionId));

        region.setName(regionUpdateDto.getName());
        region.setDescription(regionUpdateDto.getDescription());
        region.setCountry(CountryMapper.toEntity(countryService.getCountryById(regionUpdateDto.getCountryId())));
        Region savedRegion = regionRepository.save(region);
        log.info("Updated region with id: {}", regionId);
        return RegionMapper.toDto(savedRegion);
    }
    

    /**
     * Retrieves a list of all regions.
     *
     * @return a list of RegionDto objects representing all regions
     */
    @Override
    public List<RegionDto> getAllRegions() {
        log.info("Retrieving all regions");
        List<RegionDto> regionDtoList = regionRepository
                .findAll()
                .stream()
                .map(RegionMapper::toDto)
                .toList();
        log.info("Retrieved all regions");
        return regionDtoList;
    }


    /**
     * Retrieves a list of regions for a specified country.
     *
     * @param countryId the ID of the country to retrieve regions for
     * @return a list of RegionDto objects representing all regions for the given country
     * @throws ResourceNotFoundException if no regions found for the given country
     */
    @Override
    public List<RegionDto> getRegionsByCountry(Long countryId) {
        log.info("Retrieving regions for country with id: {}", countryId);
        List<RegionDto> regions = regionRepository.findByCountryId(countryId).stream().map(RegionMapper::toDto).toList();
        if (regions.isEmpty()) {
            throw new ResourceNotFoundException("No regions found for country with id: " + countryId);
        }

        log.info("Retrieved {} regions for country with id: {}", regions.size(), countryId);
        return regions;
    }


    /**
     * Retrieves a paginated list of regions for a specified country.
     *
     * @param countryId the ID of the country to retrieve regions for
     * @param pageable  the pagination parameters
     * @return a paginated list of RegionDto objects representing all regions for the given country
     * @throws ResourceNotFoundException if no regions are found for the given country
     */
    @Override
    public Page<RegionDto> getRegionsByCountry(Long countryId, Pageable pageable) {

        log.info("Retrieving regions for country with id : {}", countryId);
        Page<RegionDto> regions = regionRepository.findByCountryId(countryId, pageable).map(RegionMapper::toDto);

        if (regions.isEmpty()) {
            log.error("No regions found for country with id: {}", countryId);
            throw new ResourceNotFoundException("No regions found for country with id: " + countryId);
        }

        log.info("Retrieved {} regions of country with id: {}", regions.getTotalElements(), countryId);
        return regions;
    }


    /**
     * Retrieves a region by its ID.
     *
     * @param regionId the ID of the region to be retrieved
     * @return a RegionDto object representing the region with the given ID
     * @throws ResourceNotFoundException if no region is found with the given ID
     */
    @Override
    public RegionDto getRegionById(Long regionId) {
        log.info("Retrieving region with id: {}", regionId);
        RegionDto region = regionRepository
                .findById(regionId)
                .map(RegionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + regionId));

        log.info("Retrieved region with id: {}", regionId);
        return region;
    }


    /**
     * Creates a new region from a given {@link RegionCreateDto} object.
     *
     * @param regionCreateDto the {@link RegionCreateDto} object containing the region details
     * @return a {@link RegionDto} object representing the newly created region
     */
    @Override
    public RegionDto createRegion(RegionCreateDto regionCreateDto) {
        log.info("Creating region with name: {}", regionCreateDto.getName());
        Region region = RegionMapper.toEntity(regionCreateDto, countryService.getCountryById(regionCreateDto.getCountryId()));
        Region savedRegion = regionRepository.save(region);

        log.info("Created region with id: {}", savedRegion.getName());
        return RegionMapper.toDto(savedRegion);
    }


    /**
     * Deletes a region by its ID.
     *
     * @param regionId the ID of the region to be deleted
     * @throws ResourceNotFoundException if no region is found with the given ID
     */
    @Override
    public void deleteRegion(Long regionId) {
        log.info("Deleting region with id: {}", regionId);
        Region region = regionRepository
                .findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + regionId));
        regionRepository.delete(region);
        log.info("Deleted region with id: {}", regionId);
    }


    /**
     * Retrieves a region by its ID for a specified country and language.
     *
     * @param regionId    the ID of the region to be retrieved
     * @param countryDto  the country details for which the region is to be retrieved
     * @param languageDto the language details for localization
     * @return a RegionLocalizedDto object representing the region with the given ID for the specified country and language
     * @throws ResourceNotFoundException if no region is found with the given ID for the specified country and language
     */
    @Override
    public RegionLocalizedDto getRegionByIdAndCountryAndLanguage(Long regionId, CountryDto countryDto, LanguageDto languageDto) {
        log.info("Retrieving region with id: {} for country: {} and language: {}", regionId, countryDto.getName(), languageDto.getName());

        Region region = regionRepository.findByIdAndCountry(regionId, CountryMapper.toEntity(countryDto)).orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + regionId + " for country with name: " + countryDto.getName()));
        RegionTranslation rtr = regionTranslationRepository
                .findByRegionIdAndLanguageId(regionId, languageDto.getId()).orElse(null);

        if (rtr == null) {
            log.info("Region translation not found for region with id: {} and language: {}", regionId, languageDto.getName());
            log.info("Falling back to global language");
            LanguageDto globalLanguage = languageService.getGlobalLanguage();
            rtr = regionTranslationRepository.findByRegionIdAndLanguageId(regionId, globalLanguage.getId()).orElseThrow(() -> new ResourceNotFoundException("Region translation not found for region with id: " + regionId));
        }

        log.info("Retrieved region with id: {} for country: {} and language: {}", regionId, countryDto.getName(), languageDto.getName());
        return RegionMapper.toLocalizedDto(region, rtr);
    }

}
