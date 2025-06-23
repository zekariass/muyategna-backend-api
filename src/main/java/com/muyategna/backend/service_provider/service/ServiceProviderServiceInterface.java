package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderUpdateDto;

import java.util.List;

public interface ServiceProviderServiceInterface {
    ServiceProviderDto createServiceProvider(ServiceProviderCreateDto serviceProviderDto);

    ServiceProviderDto updateServiceProviderById(Long serviceProviderId, ServiceProviderUpdateDto serviceProviderDto);

    ServiceProviderDto getServiceProviderById(Long serviceProviderId);

    List<ServiceProviderDto> getServiceProviderServices(Long serviceProviderId);

    Boolean activateServiceProviderById(Long serviceProviderId);

    Boolean inActivateServiceProviderById(Long serviceProviderId);
}
