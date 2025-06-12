package com.muyategna.backend.professional_service.mapper;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryTranslationDto;
import com.muyategna.backend.professional_service.entity.ServiceCategory;
import com.muyategna.backend.professional_service.entity.ServiceCategoryTranslation;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;

public final class ServiceCategoryTranslationMapper {

    public static ServiceCategoryTranslationDto toDto(ServiceCategoryTranslation serviceCategoryTranslation) {
        if (serviceCategoryTranslation == null) {
            return null;
        }
        return ServiceCategoryTranslationDto.builder()
                .id(serviceCategoryTranslation.getId())
                .serviceCategoryId(serviceCategoryTranslation.getServiceCategory().getId())
                .languageId(serviceCategoryTranslation.getLanguage().getId())
                .name(serviceCategoryTranslation.getName())
                .description(serviceCategoryTranslation.getDescription())
                .createdAt(serviceCategoryTranslation.getCreatedAt())
                .updatedAt(serviceCategoryTranslation.getUpdatedAt())
                .build();
    }

    public static ServiceCategoryTranslation toEntity(ServiceCategoryTranslationDto serviceCategoryTranslationDto,
                                                      ServiceCategory serviceCategory,
                                                      Language language) {
        if (serviceCategoryTranslationDto == null) {
            return null;
        }
        if (serviceCategory == null) {
            throw new ResourceNotFoundException("ServiceCategory and Language must not be null");
        }

        if (language == null) {
            throw new ResourceNotFoundException("Language must not be null");
        }

        return getServiceCategoryTranslation(serviceCategoryTranslationDto, serviceCategory, language);
    }

    @NotNull
    private static ServiceCategoryTranslation getServiceCategoryTranslation(ServiceCategoryTranslationDto serviceCategoryTranslationDto, ServiceCategory serviceCategory, Language language) {
        ServiceCategoryTranslation serviceCategoryTranslation = new ServiceCategoryTranslation();
        serviceCategoryTranslation.setId(serviceCategoryTranslationDto.getId());
        serviceCategoryTranslation.setServiceCategory(serviceCategory);
        serviceCategoryTranslation.setLanguage(language);
        serviceCategoryTranslation.setName(serviceCategoryTranslationDto.getName());
        serviceCategoryTranslation.setDescription(serviceCategoryTranslationDto.getDescription());
        serviceCategoryTranslation.setCreatedAt(serviceCategoryTranslationDto.getCreatedAt());
        return serviceCategoryTranslation;
    }
}
