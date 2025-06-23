package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.professional_service.dto.availability.ServiceCountryAvailabilityDto;
import com.muyategna.backend.professional_service.dto.service.ServiceDto;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.mapper.ServiceMapper;
import com.muyategna.backend.professional_service.repository.ServiceRepository;
import com.muyategna.backend.professional_service.repository.ServiceTranslationRepository;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceCountryAvailabilityService serviceCountryAvailabilityService;
    private final LanguageService languageService;
    private final ServiceTranslationRepository serviceTranslationRepository;
    private final ServiceTranslationService serviceTranslationService;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceCountryAvailabilityService serviceCountryAvailabilityService, LanguageService languageService, ServiceTranslationRepository serviceTranslationRepository, ServiceTranslationService serviceTranslationService) {
        this.serviceRepository = serviceRepository;
        this.serviceCountryAvailabilityService = serviceCountryAvailabilityService;
        this.languageService = languageService;
        this.serviceTranslationRepository = serviceTranslationRepository;
        this.serviceTranslationService = serviceTranslationService;
    }

    @Override
    public Page<ServiceLocalizedDto> getAllServicesForCurrentCountry(Pageable pageable) {
        CountryDto countryDto = CountryContextHolder.getCountry();

        log.info("Getting all services for current country with ID: {}", countryDto.getId());

        List<ServiceCountryAvailabilityDto> availableServices = serviceCountryAvailabilityService
                .getActiveAvailableServicesForCountry(countryDto.getId());

        if (CollectionUtils.isEmpty(availableServices)) {
            log.warn("No available services found for country ID: {}", countryDto.getId());
            return Page.empty(pageable);
        }

        List<Long> serviceIds = availableServices.stream()
                .map(ServiceCountryAvailabilityDto::getServiceId)
                .toList();

        Page<ServiceDto> pagedServices = serviceRepository
                .findByIdIn(serviceIds, pageable)
                .map(ServiceMapper::toDto);

        List<ServiceDto> pagedServicesContent = pagedServices.getContent();

//        List<Long> pageServiceIds = pagedServicesContent.stream()
//                .map(ServiceDto::getId)
//                .toList();

        List<ServiceLocalizedDto> localizedDtoList = serviceTranslationService.getServiceLocalizedDtoList(pagedServicesContent);

        return new PageImpl<>(localizedDtoList, pageable, pagedServices.getTotalElements());
    }

//    @NotNull
//    private List<ServiceLocalizedDto> getServiceLocalizedDtoList(List<ServiceDto> serviceDtoList) {
//
//        LanguageDto languageDto = LanguageContextHolder.getLanguage();
//
//        log.info("Retrieving localized services for language: {}", languageDto.getLocale());
//        List<Long> serviceIds = serviceDtoList.stream()
//                .map(ServiceDto::getId)
//                .toList();
//
//        // Get translations for current language
//        List<ServiceTranslation> translations = serviceTranslationRepository
//                .findByServiceIdInAndLanguageId(serviceIds, languageDto.getId());
//
//        // Create a map for quick lookup of translations by service ID
//        Map<Long, ServiceTranslation> translationMap = translations.stream()
//                .collect(Collectors.toMap(tr -> tr.getService().getId(), Function.identity()));
//
//        // Fallback for missing translations
//        List<Long> missingIds = serviceIds.stream()
//                .filter(id -> !translationMap.containsKey(id))
//                .toList();
//
//        List<ServiceTranslation> fallbackTranslations = serviceTranslationRepository
//                .findByServiceIdInAndLanguageId(missingIds, languageService.getGlobalLanguage().getId());
//
//        // Combine DTOs with translations
//        List<ServiceLocalizedDto> localizedDtos = serviceDtoList.stream()
//                .map(dto -> {
//                    ServiceTranslation tr = translationMap.getOrDefault(dto.getId(), null);
//                    if (tr == null) {
//                        tr = fallbackTranslations.stream()
//                                .filter(fb -> fb.getService().getId().equals(dto.getId()))
//                                .findFirst()
//                                .orElse(null);
//                    }
//                    return ServiceMapper.toLocalizedDto(dto, tr);
//                })
//                .toList();
//        log.info("Retrieved {} localized service DTOs for language: {}", localizedDtos.size(), languageDto.getLocale());
//        return localizedDtos;
//    }


    @Override
    public ServiceLocalizedDto getServiceByIdForCurrentCountry(Long serviceId) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        log.info("Retrieving service with ID: {} for current country with ID: {}", serviceId, countryDto.getId());
        boolean isServiceAvailable = serviceCountryAvailabilityService
                .isActiveServiceExistsForCountry(serviceId, countryDto.getId());

        if (!isServiceAvailable) {
            log.warn("Service with ID: {} is not available in country with ID: {}", serviceId, countryDto.getId());
            throw new ResourceNotFoundException("Service with ID: " + serviceId + " is not available in country with ID: " + countryDto.getId());
        }

        ServiceTranslation translation = serviceTranslationRepository
                .findByServiceIdAndLanguageId(serviceId, languageDto.getId())
                .orElse(null);

        if (translation == null) {
            LanguageDto globalLanguage = languageService.getGlobalLanguage();
            translation = serviceTranslationRepository
                    .findByServiceIdAndLanguageId(serviceId, globalLanguage.getId())
                    .orElse(null);
        }

        ServiceTranslation finalTranslation = translation;
        return serviceRepository
                .findById(serviceId)
                .map(service -> ServiceMapper.toLocalizedDto(ServiceMapper.toDto(service), finalTranslation))
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));
    }

    @Override
    public Optional<com.muyategna.backend.professional_service.entity.Service> getServiceById(Long serviceId) {
        return serviceRepository.findById(serviceId);
    }


    @Override
    public List<ServiceLocalizedDto> getServicesByNameForCurrentCountry(String serviceName) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        log.info("Retrieving services by name: '{}' for current country with ID: {}", serviceName, countryDto.getId());
        List<ServiceCountryAvailabilityDto> availableServices = serviceCountryAvailabilityService
                .getActiveAvailableServicesForCountryByName(serviceName, countryDto.getId());

        if (CollectionUtils.isEmpty(availableServices)) {
            log.warn("No available services found for name: '{}' in country with ID: {}", serviceName, countryDto.getId());
            return List.of();
        }


        log.info("Found {} available services by name: '{}' for current country with ID: {}", availableServices.size(), serviceName, countryDto.getId());

        List<Long> serviceIds = availableServices.stream()
                .map(ServiceCountryAvailabilityDto::getServiceId)
                .toList();

        List<ServiceDto> services = serviceRepository
                .findAllById(serviceIds)
                .stream()
                .map(ServiceMapper::toDto)
                .toList();

        log.info("Retrieved {} services by name: '{}' for current country with ID: {}", services.size(), serviceName, countryDto.getId());

        if (CollectionUtils.isEmpty(services)) {
            log.warn("No services found by name: '{}' for current country with ID: {}", serviceName, countryDto.getId());
            return List.of();
        }

        List<ServiceLocalizedDto> localizedDtoList = serviceTranslationService.getServiceLocalizedDtoList(services);

        log.info("Returning {} localized services by name: '{}' for current country with ID: {}", localizedDtoList.size(), serviceName, countryDto.getId());
        return localizedDtoList;
    }

    @Override
    public List<ServiceLocalizedDto> getServicesByCategoryForCurrentCountry(Long categoryId) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        log.info("Retrieving services by category ID: {} for current country with ID: {}", categoryId, countryDto.getId());
        List<ServiceCountryAvailabilityDto> availableServices = serviceCountryAvailabilityService
                .getActiveAvailableServicesForCountryByCategory(categoryId, countryDto.getId());

        if (CollectionUtils.isEmpty(availableServices)) {
            log.warn("No available services found for category ID: {} in country with ID: {}", categoryId, countryDto.getId());
            return List.of();
        }

        log.info("Found {} available services by category ID: {} for current country with ID: {}", availableServices.size(), categoryId, countryDto.getId());
        List<Long> serviceIds = availableServices.stream()
                .map(ServiceCountryAvailabilityDto::getServiceId)
                .toList();

        List<ServiceDto> services = serviceRepository
                .findByIdIn(serviceIds)
                .stream()
                .map(ServiceMapper::toDto)
                .toList();

        if (CollectionUtils.isEmpty(services)) {
            log.warn("No services found by category ID: {} for current country with ID: {}", categoryId, countryDto.getId());
            return List.of();
        }
        log.info("Retrieved {} services by category ID: {} for current country with ID: {}", services.size(), categoryId, countryDto.getId());

        List<ServiceLocalizedDto> localizedDtoList = serviceTranslationService.getServiceLocalizedDtoList(services);

        if (CollectionUtils.isEmpty(localizedDtoList)) {
            log.warn("No translated services found by category ID: {} for current country with ID: {}", categoryId, countryDto.getId());
            return List.of();
        }
        log.info("Returning {} localized services by category ID: {} for current country with ID: {}", localizedDtoList.size(), categoryId, countryDto.getId());
        return localizedDtoList;
    }


    @Override
    public Page<ServiceLocalizedDto> getPagedServicesByNameForCurrentCountry(String serviceName, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ServiceLocalizedDto> getPagedServicesByCategoryForCurrentCountry(Long categoryId, Pageable pageable) {
        return null;
    }
}
