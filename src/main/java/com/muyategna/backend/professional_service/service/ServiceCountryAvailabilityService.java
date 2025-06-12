package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.professional_service.dto.availability.ServiceCountryAvailabilityDto;

import java.util.List;

public interface ServiceCountryAvailabilityService {
    List<ServiceCountryAvailabilityDto> getActiveAvailableServicesForCountry(Long id);

    boolean isActiveServiceExistsForCountry(Long serviceId, Long countryId);

    List<ServiceCountryAvailabilityDto> getActiveAvailableServicesForCountryByName(String serviceName, Long countryId);

    List<ServiceCountryAvailabilityDto> getActiveAvailableServicesForCountryByCategory(Long categoryId, Long countryId);
}
