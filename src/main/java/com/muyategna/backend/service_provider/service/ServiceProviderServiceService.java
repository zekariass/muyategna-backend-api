package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import jakarta.validation.Valid;

import java.util.List;

public interface ServiceProviderServiceService {
    List<ServiceProviderService> createServiceProviderServices(List<ServiceProviderService> providerServices);

    List<ServiceProviderServiceLocalizedDto> getLocalizedServiceProviderServices(Long serviceProviderId);

    ServiceProviderServiceLocalizedDto getLocalizedServiceProviderServiceByIdAndServiceProviderId(Long serviceProviderServiceId, Long serviceProviderId);

    ServiceProviderServiceLocalizedDto activateServiceProviderService(Long serviceProviderServiceId, Long serviceProviderId);

    ServiceProviderServiceLocalizedDto inactivateServiceProviderService(Long serviceProviderServiceId, Long serviceProviderId);

    ServiceProviderServiceLocalizedDto createServiceProviderService(Long serviceProviderId, @Valid ServiceProviderServiceCreateDto serviceProviderServiceDto, Country country);

}
