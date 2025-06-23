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

import java.util.List;
import java.util.Map;
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

    @Override
    public List<ServiceLocalizedDto> getServiceLocalizedDtoList(List<ServiceDto> serviceDtoList) {

        LanguageDto languageDto = LanguageContextHolder.getLanguage();

        log.info("Retrieving localized services for language: {}", languageDto.getLocale());
        List<Long> serviceIds = serviceDtoList.stream()
                .map(ServiceDto::getId)
                .toList();

        // Get translations for current language
        List<ServiceTranslation> translations = serviceTranslationRepository
                .findByServiceIdInAndLanguageId(serviceIds, languageDto.getId());

        // Create a map for quick lookup of translations by service ID
        Map<Long, ServiceTranslation> translationMap = translations.stream()
                .collect(Collectors.toMap(tr -> tr.getService().getId(), Function.identity()));

        // Fallback for missing translations
        List<Long> missingIds = serviceIds.stream()
                .filter(id -> !translationMap.containsKey(id))
                .toList();

        List<ServiceTranslation> fallbackTranslations = serviceTranslationRepository
                .findByServiceIdInAndLanguageId(missingIds, languageService.getGlobalLanguage().getId());

        // Combine DTOs with translations
        List<ServiceLocalizedDto> localizedDtos = serviceDtoList.stream()
                .map(dto -> {
                    ServiceTranslation tr = translationMap.getOrDefault(dto.getId(), null);
                    if (tr == null) {
                        tr = fallbackTranslations.stream()
                                .filter(fb -> fb.getService().getId().equals(dto.getId()))
                                .findFirst()
                                .orElse(null);
                    }
                    return ServiceMapper.toLocalizedDto(dto, tr);
                })
                .toList();
        log.info("Retrieved {} localized service DTOs for language: {}", localizedDtos.size(), languageDto.getLocale());
        return localizedDtos;
    }


    @Override
    public ServiceTranslation getTranslationByServiceId(Long serviceId) {
        log.info("Retrieving translation for service ID: {}", serviceId);
        LanguageDto languageDto = LanguageContextHolder.getLanguage();
        ServiceTranslation translation = serviceTranslationRepository
                .findByServiceIdAndLanguageId(serviceId, languageDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Translation not found for service ID: " + serviceId));
        log.info("Retrieved translation for service ID: {}", serviceId);
        return translation;
    }
}
