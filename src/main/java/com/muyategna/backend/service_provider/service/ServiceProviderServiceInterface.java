package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.service_provider.dto.service_provider.NearbyServiceProviderListDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderUpdateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;
import com.muyategna.backend.service_provider.repository.projection.ProviderServiceStatProjection;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServiceProviderServiceInterface {
    ServiceProviderDto createServiceProvider(ServiceProviderCreateDto serviceProviderDto);

    ServiceProviderDto updateServiceProviderById(Long serviceProviderId, ServiceProviderUpdateDto serviceProviderDto);

    ServiceProviderDto getServiceProviderById(Long serviceProviderId);

    List<ServiceProviderDto> getServiceProviderServices(Long serviceProviderId);

    Boolean activateServiceProviderById(Long serviceProviderId);

    Boolean inActivateServiceProviderById(Long serviceProviderId);

    List<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> getServiceProviderServicesByServiceAndLocation(Long serviceId, String deliveryMode, String geoPointWkt, Double radius, Long currentCountryId, int queryLimit);

    Set<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> getServiceProviderServicesByServiceAndLocation(Long serviceId, String deliveryMode, Point geoPoint, double radius, List<Long> excludedServiceProviderIds, Long currentCountryId);

    List<ServiceProviderService> getServiceProviderServicesByProviderIdAndIsActive(Long serviceProviderId, boolean b);

    ServiceProviderServiceDto getServiceProviderServiceByProviderIdAndServiceId(Long serviceProviderId, Long serviceId);

    List<ServiceProvider> findServiceProvidersByIdsWithNotificationPreferences(List<Long> serviceProviderIds);

    List<ProviderServiceStatProjection> getServiceProviderServicesByLocationAndService(Long serviceId, String deliveryMode, String jobRequestGeoPoint, double radiusMeters, Long countryId, int queryLimit);

    List<NearbyServiceProviderListDto> getServiceProviderList(List<Long> rankedServiceProviderIds);

    List<NearbyServiceProviderForAJobProjection> getNearbyServiceProvidersForCustomerView(List<Long> rankedServiceProviderIds, JobLead jobLead, String jobGeoPoint);

    boolean existsActiveServiceProviderServiceByProviderIdAndServiceId(Long serviceProviderId, Long serviceId);

    Optional<ServiceProvider> getServiceProviderByIdWithNotificationPreference(Long providerId);

    ServiceProvider getServiceProviderWithBusinessAddressById(Long providerId);

    Optional<ServiceProvider> getServiceProvider(Long serviceProviderId);
}
