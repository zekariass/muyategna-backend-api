package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;

public final class ServiceProviderServiceMapper {

    public static ServiceProviderServiceDto toDto(ServiceProviderService entity) {
        return ServiceProviderServiceDto.builder()
                .id(entity.getId())
                .serviceId(entity.getService().getId())
                .providerId(entity.getProvider().getId())
                .isActive(entity.getIsActive())
                .linkedAt(entity.getLinkedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ServiceProviderServiceLocalizedDto toLocalizedDto(ServiceProviderService entity,
                                                                    ServiceLocalizedDto serviceLocalizedDto) {
        return ServiceProviderServiceLocalizedDto.builder()
                .id(entity.getId())
                .service(serviceLocalizedDto)
                .providerId(entity.getProvider().getId())
                .isActive(entity.getIsActive())
                .linkedAt(entity.getLinkedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static ServiceProviderService toEntity(ServiceProviderServiceDto dto,
                                                  Service service,
                                                  ServiceProvider provider) {
        if (dto == null) {
            return null;
        }
        ServiceProviderService entity = new ServiceProviderService();
        entity.setId(dto.getId());
        entity.setIsActive(dto.getIsActive());
        entity.setLinkedAt(dto.getLinkedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setService(service);
        entity.setProvider(provider);
        return entity;
    }


    public static ServiceProviderService toEntity(ServiceProviderServiceUpdateDto dto,
                                                  Service service,
                                                  ServiceProvider provider) {
        if (dto == null) {
            return null;
        }
        ServiceProviderService entity = new ServiceProviderService();
        entity.setId(dto.getId());
        entity.setIsActive(dto.getIsActive());
        entity.setService(service);
        entity.setProvider(provider);
        return entity;
    }


    public static ServiceProviderService toEntity(ServiceProviderServiceCreateDto dto,
                                                  Service service,
                                                  ServiceProvider provider) {
        if (dto == null) {
            return null;
        }
        ServiceProviderService entity = new ServiceProviderService();
        entity.setService(service);
        entity.setProvider(provider);
        return entity;
    }

}
