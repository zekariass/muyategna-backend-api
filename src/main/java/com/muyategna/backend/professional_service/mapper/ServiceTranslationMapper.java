package com.muyategna.backend.professional_service.mapper;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.professional_service.dto.service.ServiceTranslationDto;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.system.exception.ResourceNotFoundException;

public final class ServiceTranslationMapper {

    public static ServiceTranslationDto toDto(ServiceTranslation serviceTranslation) {
        if (serviceTranslation == null) {
            return null;
        }
        return ServiceTranslationDto.builder()
                .id(serviceTranslation.getId())
                .serviceId(serviceTranslation.getService().getId())
                .languageId(serviceTranslation.getLanguage().getId())
                .name(serviceTranslation.getName())
                .description(serviceTranslation.getDescription())
                .createdAt(serviceTranslation.getCreatedAt())
                .updatedAt(serviceTranslation.getUpdatedAt())
                .build();
    }

    public static ServiceTranslation toEntity(ServiceTranslationDto serviceTranslationDto,
                                              Service service,
                                              Language language) {
        if (serviceTranslationDto == null) {
            return null;
        }

        if (service == null) {
            throw new ResourceNotFoundException("Service not found with ID: " + serviceTranslationDto.getServiceId());
        }

        if (language == null) {
            throw new ResourceNotFoundException("Language not found with ID: " + serviceTranslationDto.getLanguageId());
        }

        ServiceTranslation serviceTranslation = new ServiceTranslation();
        serviceTranslation.setId(serviceTranslationDto.getId());
        serviceTranslation.setService(service);
        serviceTranslation.setLanguage(language);
        serviceTranslation.setName(serviceTranslationDto.getName());
        serviceTranslation.setDescription(serviceTranslationDto.getDescription());
        serviceTranslation.setCreatedAt(serviceTranslationDto.getCreatedAt());
        return serviceTranslation;
    }
}
