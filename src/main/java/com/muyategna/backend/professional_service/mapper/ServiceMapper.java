package com.muyategna.backend.professional_service.mapper;

import com.muyategna.backend.professional_service.dto.service.ServiceDto;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.professional_service.entity.ServiceCategory;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.system.exception.ResourceNotFoundException;

import java.util.List;

public final class ServiceMapper {

    public static ServiceDto toDto(Service service) {
        if (service == null) {
            return null;
        }
        return ServiceDto.builder()
                .id(service.getId())
                .serviceCategoryId(service.getServiceCategory().getId())
                .name(service.getName())
                .description(service.getDescription())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }

    public static Service toEntity(ServiceDto serviceDto,
                                   ServiceCategory serviceCategory) {
        if (serviceDto == null) {
            return null;
        }

        if (serviceCategory == null) {
            throw new ResourceNotFoundException("ServiceCategory cannot be null");
        }

        Service service = new Service();
        service.setId(serviceDto.getId());
        service.setServiceCategory(serviceCategory);
        service.setName(serviceDto.getName());
        service.setDescription(serviceDto.getDescription());
        service.setCreatedAt(serviceDto.getCreatedAt());
        return service;
    }


    public static ServiceLocalizedDto toLocalizedDto(ServiceDto service, ServiceTranslation serviceTranslation) {
        if (service == null) {
            return null;
        }

        if (serviceTranslation == null) {
            return ServiceLocalizedDto.builder()
                    .id(service.getId())
                    .serviceCategoryId(service.getServiceCategoryId())
                    .name(service.getName())
                    .description(service.getDescription())
                    .createdAt(service.getCreatedAt())
                    .updatedAt(service.getUpdatedAt())
                    .build();
        }

        return ServiceLocalizedDto.builder()
                .id(service.getId())
                .serviceCategoryId(service.getServiceCategoryId())
                .name(serviceTranslation.getName())
                .description(serviceTranslation.getDescription())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }


    public static ServiceLocalizedDto toLocalizedDto(Service service, ServiceTranslation serviceTranslation) {
        if (service == null) {
            return null;
        }

        if (serviceTranslation == null) {
            throw new ResourceNotFoundException("ServiceTranslation cannot be null for service: " + service.getId());
        }

        return ServiceLocalizedDto.builder()
                .id(service.getId())
                .serviceCategoryId(service.getServiceCategory().getId())
                .name(serviceTranslation.getName())
                .description(serviceTranslation.getDescription())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }


    public static List<ServiceDto> toDtoList(List<Service> services) {
        if (services == null || services.isEmpty()) {
            return List.of();
        }
        return services.stream()
                .map(ServiceMapper::toDto)
                .toList();
    }
}
