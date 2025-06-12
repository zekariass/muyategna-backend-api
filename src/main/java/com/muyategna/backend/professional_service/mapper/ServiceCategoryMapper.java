package com.muyategna.backend.professional_service.mapper;

import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryDto;
import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryLocalizedDto;
import com.muyategna.backend.professional_service.entity.ServiceCategory;
import com.muyategna.backend.professional_service.entity.ServiceCategoryTranslation;

public final class ServiceCategoryMapper {

    public static ServiceCategoryDto toDto(ServiceCategory serviceCategory) {
        if (serviceCategory == null) {
            return null;
        }
        return ServiceCategoryDto.builder()
                .id(serviceCategory.getId())
                .name(serviceCategory.getName())
                .description(serviceCategory.getDescription())
                .createdAt(serviceCategory.getCreatedAt())
                .updatedAt(serviceCategory.getUpdatedAt())
                .build();
    }

    public static ServiceCategory toEntity(ServiceCategoryDto serviceCategoryDto) {
        if (serviceCategoryDto == null) {
            return null;
        }

        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setId(serviceCategoryDto.getId());
        serviceCategory.setName(serviceCategoryDto.getName());
        serviceCategory.setDescription(serviceCategoryDto.getDescription());
        serviceCategory.setCreatedAt(serviceCategoryDto.getCreatedAt());
        return serviceCategory;
    }

    public static ServiceCategoryLocalizedDto toLocalizedDto(ServiceCategory serviceCategory,
                                                             ServiceCategoryTranslation translation) {
        if (serviceCategory == null) {
            return null;
        }

        if (translation == null) {
            return ServiceCategoryLocalizedDto.builder()
                    .id(serviceCategory.getId())
                    .name(serviceCategory.getName())
                    .description(serviceCategory.getDescription())
                    .createdAt(serviceCategory.getCreatedAt())
                    .updatedAt(serviceCategory.getUpdatedAt())
                    .build();
        }

        return ServiceCategoryLocalizedDto.builder()
                .id(serviceCategory.getId())
                .name(translation.getName())
                .description(translation.getDescription())
                .createdAt(serviceCategory.getCreatedAt())
                .updatedAt(serviceCategory.getUpdatedAt())
                .build();
    }
}
