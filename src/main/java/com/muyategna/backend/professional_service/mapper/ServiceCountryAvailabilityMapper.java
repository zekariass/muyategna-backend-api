package com.muyategna.backend.professional_service.mapper;

import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.professional_service.dto.availability.ServiceCountryAvailabilityDto;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.professional_service.entity.ServiceCountryAvailability;
import com.muyategna.backend.system.exception.ResourceNotFoundException;

public final class ServiceCountryAvailabilityMapper {

    public static ServiceCountryAvailabilityDto toDto(ServiceCountryAvailability dto) {
        if (dto == null) {
            return null;
        }
        return ServiceCountryAvailabilityDto.builder()
                .id(dto.getId())
                .serviceId(dto.getService().getId())
                .countryId(dto.getCountry().getId())
                .isActive(dto.getIsActive())
                .priceModel(dto.getPriceModel())
                .basePrice(dto.getBasePrice())
                .notes(dto.getNotes())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static ServiceCountryAvailability toEntity(ServiceCountryAvailabilityDto dto,
                                                      Service service,
                                                      Country country) {
        if (dto == null) {
            return null;
        }

        if (country == null) {
            throw new ResourceNotFoundException("Country cannot be null");
        }

        if (service == null) {
            throw new ResourceNotFoundException("Service cannot be null");
        }

        ServiceCountryAvailability serviceCountryAvailability = new ServiceCountryAvailability();
        serviceCountryAvailability.setId(dto.getId());
        serviceCountryAvailability.setService(service);
        serviceCountryAvailability.setCountry(country);
        serviceCountryAvailability.setIsActive(dto.getIsActive());
        serviceCountryAvailability.setPriceModel(dto.getPriceModel());
        serviceCountryAvailability.setBasePrice(dto.getBasePrice());
        serviceCountryAvailability.setNotes(dto.getNotes());
        serviceCountryAvailability.setCreatedAt(dto.getCreatedAt());
        return serviceCountryAvailability;
    }
}
