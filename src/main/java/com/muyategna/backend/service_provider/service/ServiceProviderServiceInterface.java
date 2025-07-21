package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderUpdateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceProjection;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Set;

public interface ServiceProviderServiceInterface {
    ServiceProviderDto createServiceProvider(ServiceProviderCreateDto serviceProviderDto);

    ServiceProviderDto updateServiceProviderById(Long serviceProviderId, ServiceProviderUpdateDto serviceProviderDto);

    ServiceProviderDto getServiceProviderById(Long serviceProviderId);

    List<ServiceProviderDto> getServiceProviderServices(Long serviceProviderId);

    Boolean activateServiceProviderById(Long serviceProviderId);

    Boolean inActivateServiceProviderById(Long serviceProviderId);

    Set<ServiceProviderServiceWithDistanceProjection> getServiceProviderServicesByServiceAndLocation(Long serviceId, Point geoPoint, Double radius);

    Set<ServiceProviderServiceWithDistanceProjection> getServiceProviderServicesByServiceAndLocation(Long id, Point geoPoint, double radius, List<Long> excludedServiceProviderIds);

    List<ServiceProviderService> getServiceProviderServicesByProviderIdAndIsActive(Long serviceProviderId, boolean b);

    ServiceProviderServiceDto getServiceProviderServiceByProviderIdAndServiceId(Long serviceProviderId, Long serviceId);

}
