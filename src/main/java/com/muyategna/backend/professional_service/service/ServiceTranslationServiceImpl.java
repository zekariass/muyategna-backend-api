package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.professional_service.dto.service.ServiceDto;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.mapper.ServiceMapper;
import com.muyategna.backend.professional_service.repository.ServiceTranslationRepository;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceTranslationServiceImpl implements ServiceTranslationService {

    private final ServiceTranslationRepository serviceTranslationRepository;
    private final LanguageService languageService;

    @Autowired
    public ServiceTranslationServiceImpl(ServiceTranslationRepository serviceTranslationRepository, LanguageService languageService) {
        this.serviceTranslationRepository = serviceTranslationRepository;
        this.languageService = languageService;
    }

//    @Override
//    public List<ServiceLocalizedDto> getServiceLocalizedDtoList(List<ServiceDto> serviceDtoList) {
//
//        log.info("Retrieving localized services");
//        List<Long> serviceIds = serviceDtoList.stream()
//                .map(ServiceDto::getId)
//                .toList();
//
//        LanguageDto languageDto = LanguageContextHolder.getLanguage();
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
    public List<ServiceLocalizedDto> getServiceLocalizedDtoList(List<ServiceDto> serviceDtoList) {
        log.info("Retrieving localized services");

        List<Long> serviceIds = serviceDtoList.stream()
                .map(ServiceDto::getId)
                .collect(Collectors.toList());

        LanguageDto languageDto = LanguageContextHolder.getLanguage();

        List<ServiceTranslation> translations = serviceTranslationRepository
                .findByServiceIdInAndLanguageId(serviceIds, languageDto.getId());

        Map<Long, ServiceTranslation> translationMap = translations.stream()
                .collect(Collectors.toMap(tr -> tr.getService().getId(), Function.identity()));

        List<Long> missingIds = serviceIds.stream()
                .filter(id -> !translationMap.containsKey(id))
                .collect(Collectors.toList());

        long fallbackLanguageId = languageService.getGlobalLanguage().getId();
        List<ServiceTranslation> fallbackTranslations = serviceTranslationRepository
                .findByServiceIdInAndLanguageId(missingIds, fallbackLanguageId);

        Map<Long, ServiceTranslation> fallbackMap = fallbackTranslations.stream()
                .collect(Collectors.toMap(ft -> ft.getService().getId(), Function.identity()));

        List<ServiceLocalizedDto> localizedDtos = serviceDtoList.stream()
                .map(dto -> {
                    ServiceTranslation tr = translationMap.get(dto.getId());
                    if (tr == null) {
                        tr = fallbackMap.get(dto.getId());
                    }
                    return ServiceMapper.toLocalizedDto(dto, tr);
                })
                .collect(Collectors.toList());

        log.info("Retrieved {} localized service DTOs for language: {}", localizedDtos.size(), languageDto.getLocale());
        return localizedDtos;
    }


    @Override
    public ServiceTranslation getTranslationByServiceId(Long serviceId) {
        log.info("Retrieving translation for service ID: {}", serviceId);
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        ServiceTranslation translation = serviceTranslationRepository
                .findByServiceIdAndLanguageId(serviceId, languageDto.getId())
                .orElse(null);

        if (translation == null) {
            log.warn("No translation found for service ID: {} in language: {}", serviceId, languageDto.getLocale());
            // Fallback to global language if no translation found
            translation = serviceTranslationRepository
                    .findByServiceIdAndLanguageId(serviceId, languageService.getGlobalLanguage().getId())
                    .orElse(null);
            if (translation == null) {
                log.error("No fallback translation found for service ID: {}", serviceId);
                throw new ResourceNotFoundException("No translation found for service ID: " + serviceId);
            }
        }

        log.info("Retrieved translation for service ID: {}", serviceId);
        return translation;
    }

    @Override
    public List<ServiceTranslation> getTranslationByServiceIdList(List<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return Collections.emptyList();
        }

        LanguageDto currentLanguage = LanguageContextHolder.getLanguage();
        Long currentLangId = currentLanguage.getId();
        Long fallbackLangId = languageService.getGlobalLanguage().getId();

        // Fetch both current and fallback translations in a single query
        List<ServiceTranslation> allTranslations = serviceTranslationRepository
                .findByServiceIdInAndLanguageIdIn(serviceIds, List.of(currentLangId, fallbackLangId));

        // Build map: prefer current language, fallback if not found
        Map<Long, ServiceTranslation> translationMap = new HashMap<>();
        for (ServiceTranslation tr : allTranslations) {
            Long serviceId = tr.getService().getId();

            // Prioritize current language translation
            if (!translationMap.containsKey(serviceId) || tr.getLanguage().getId().equals(currentLangId)) {
                translationMap.put(serviceId, tr);
            }
        }

        // Log missing service IDs
        List<Long> missingIds = serviceIds.stream()
                .filter(id -> !translationMap.containsKey(id))
                .collect(Collectors.toList());

        if (!missingIds.isEmpty()) {
            log.warn("No translations found (even fallback) for service IDs: {}", missingIds);
        }

        // Preserve order and filter nulls
        return serviceIds.stream()
                .map(translationMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
