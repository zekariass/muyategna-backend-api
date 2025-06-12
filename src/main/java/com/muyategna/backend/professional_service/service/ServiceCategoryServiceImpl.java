package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryDto;
import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryLocalizedDto;
import com.muyategna.backend.professional_service.entity.ServiceCategory;
import com.muyategna.backend.professional_service.entity.ServiceCategoryTranslation;
import com.muyategna.backend.professional_service.mapper.ServiceCategoryMapper;
import com.muyategna.backend.professional_service.repository.ServiceCategoryRepository;
import com.muyategna.backend.professional_service.repository.ServiceCategoryTranslationRepository;
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
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository serviceCategoryRepository;
    private final ServiceCategoryTranslationRepository serviceCategoryTranslationRepository;
    private final LanguageService languageService;

    @Autowired
    public ServiceCategoryServiceImpl(ServiceCategoryRepository serviceCategoryRepository, ServiceCategoryTranslationRepository serviceCategoryTranslationRepository, LanguageService languageService) {
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.serviceCategoryTranslationRepository = serviceCategoryTranslationRepository;
        this.languageService = languageService;
    }


    /**
     * Get service category by its ID.
     *
     * @param categoryId the ID of the service category.
     * @return the service category if found, otherwise throws ResourceNotFoundException.
     */
    @Override
    public ServiceCategoryLocalizedDto getLocalizedServiceCategoryById(Long categoryId) {
        LanguageDto language = LanguageContextHolder.getLanguage();
        log.info("Retrieving service category with id: {}", categoryId);
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Service category not found with id: " + categoryId));
        log.info("Retrieved service category for id: {}", categoryId);

        ServiceCategoryTranslation translation = serviceCategoryTranslationRepository.findByServiceCategoryIdAndLanguageId(categoryId, language.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service category translation not found for id: " + categoryId + " and language: " + language.getId()));

        return ServiceCategoryMapper.toLocalizedDto(serviceCategory, translation);
    }

    /**
     * Get all service categories.
     *
     * @return a list of all service categories.
     */
    @Override
    public List<ServiceCategoryLocalizedDto> getAllLocalizedServiceCategories() {
        LanguageDto language = LanguageContextHolder.getLanguage();
        log.info("Retrieving all service categories for language: {}", language.getLocale());
        List<ServiceCategoryDto> serviceCategories = serviceCategoryRepository.findAll()
                .stream()
                .map(ServiceCategoryMapper::toDto)
                .toList();
        log.info("Retrieved {} service categories", serviceCategories.size());

        List<Long> categoryIds = serviceCategories.stream().map(ServiceCategoryDto::getId).toList();
        List<ServiceCategoryTranslation> translations = serviceCategoryTranslationRepository.findByServiceCategoryIdInAndLanguageId(categoryIds, language.getId());

        Map<Long, ServiceCategoryTranslation> translationMap = translations.stream()
                .collect(Collectors.toMap(tr -> tr.getServiceCategory().getId(), Function.identity()));

        List<Long> fallbackIds = categoryIds
                .stream()
                .filter(id -> !translationMap.containsKey(id))
                .toList();

        LanguageDto globalLanguage = languageService.getGlobalLanguage();
        List<ServiceCategoryTranslation> fallbackTranslations = serviceCategoryTranslationRepository
                .findByServiceCategoryIdInAndLanguageId(fallbackIds, globalLanguage.getId());

//        List<ServiceCategoryTranslation> allTranslations = Stream.concat(translations.stream(), fallbackTranslations.stream()).toList();
        List<ServiceCategoryLocalizedDto> localizedCategories = serviceCategories.stream()
                .map(category -> {
                    ServiceCategoryTranslation translation = translationMap.getOrDefault(category.getId(), null);
                    if (translation == null) {
                        translation = fallbackTranslations.stream()
                                .filter(tr -> tr.getServiceCategory().getId().equals(category.getId()))
                                .findFirst()
                                .orElse(null);
                    }
                    return ServiceCategoryMapper.toLocalizedDto(ServiceCategoryMapper.toEntity(category), translation);
                })
                .toList();
        log.info("Retrieved {} localized service categories", localizedCategories.size());
        return localizedCategories;
    }
}
