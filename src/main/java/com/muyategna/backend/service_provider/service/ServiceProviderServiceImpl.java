package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.billing.service.ServiceProviderWalletService;
import com.muyategna.backend.common.entity.LegalDocument;
import com.muyategna.backend.common.service.LegalDocumentService;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.exception.ServiceNotSubscribedException;
import com.muyategna.backend.location.dto.address.AddressCreateDto;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.location.service.AddressService;
import com.muyategna.backend.professional_service.service.ServiceService;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.service_provider.dto.service_provider.NearbyServiceProviderListDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderUpdateDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoCreateDto;
import com.muyategna.backend.service_provider.entity.*;
import com.muyategna.backend.service_provider.mapper.ServiceProviderAgreementMapper;
import com.muyategna.backend.service_provider.mapper.ServiceProviderMapper;
import com.muyategna.backend.service_provider.mapper.ServiceProviderServiceMapper;
import com.muyategna.backend.service_provider.mapper.ServiceProviderTaxInfoMapper;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.service_provider.repository.ServiceProviderServiceRepository;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;
import com.muyategna.backend.service_provider.repository.projection.ProviderServiceStatProjection;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.AuthUtils;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class ServiceProviderServiceImpl implements ServiceProviderServiceInterface {

    private final ServiceProviderServiceRepository serviceProviderServiceRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceProviderServiceService serviceProviderServiceService;
    private final AddressService addressService;
    private final ServiceService serviceService;
    private final ServiceProviderTaxInfoService serviceProviderTaxInfoService;
    private final LegalDocumentService legalDocumentService;
    private final ServiceProviderAgreementService serviceProviderAgreementService;
    private final UserProfileRepository userProfileRepository;
    private final ServiceEmployeeService serviceEmployeeService;
    private final ServiceProviderRoleService serviceProviderRoleService;
    private final ServiceEmployeeRoleService serviceEmployeeRoleService;
    private final ServiceProviderWalletService walletService;

    public ServiceProviderServiceImpl(ServiceProviderServiceRepository serviceProviderServiceRepository, ServiceProviderRepository serviceProviderRepository,
                                      ServiceProviderServiceService serviceProviderServiceService,
                                      AddressService addressService, ServiceService serviceService,
                                      ServiceProviderTaxInfoService serviceProviderTaxInfoService,
                                      LegalDocumentService legalDocumentService,
                                      ServiceProviderAgreementService serviceProviderAgreementService,
                                      UserProfileRepository userProfileRepository,
                                      ServiceEmployeeService serviceEmployeeService,
                                      ServiceProviderRoleService serviceProviderRoleService,
                                      ServiceEmployeeRoleService serviceEmployeeRoleService, ServiceProviderWalletService walletService) {
        this.serviceProviderServiceRepository = serviceProviderServiceRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceProviderServiceService = serviceProviderServiceService;
        this.addressService = addressService;
        this.serviceService = serviceService;
        this.serviceProviderTaxInfoService = serviceProviderTaxInfoService;
        this.legalDocumentService = legalDocumentService;
        this.serviceProviderAgreementService = serviceProviderAgreementService;
        this.userProfileRepository = userProfileRepository;
        this.serviceEmployeeService = serviceEmployeeService;
        this.serviceProviderRoleService = serviceProviderRoleService;
        this.serviceEmployeeRoleService = serviceEmployeeRoleService;
        this.walletService = walletService;
    }

    @Transactional
    @Override
    public ServiceProviderDto createServiceProvider(ServiceProviderCreateDto serviceProviderDto) {
        List<ServiceProviderServiceCreateDto> serviceProviderServices = serviceProviderDto.getSubscribedServices();
        AddressCreateDto businessAddress = serviceProviderDto.getBusinessAddressCreateDto();
        ServiceProviderTaxInfoCreateDto taxInfo = serviceProviderDto.getServiceProviderTaxInfoCreateDto();
        List<ServiceProviderAgreementCreateDto> agreements = serviceProviderDto.getServiceProviderAgreementCreateDto();

        UUID loggedInUserId = AuthUtils.getLoggedInUserId();
        if (loggedInUserId == null) {
            throw new RuntimeException("User not logged in");
        }

        // Check if the logged-in user is already linked to a service provider
        Boolean isUserAlreadyLinkedToServiceProvider = serviceEmployeeService
                .isUserAlreadyLinkedToServiceProvider(loggedInUserId);
        if (isUserAlreadyLinkedToServiceProvider) {
            throw new BadRequestException("Unable to create a service provider. The logged-in User is already linked to a service provider");
        }

        // Save business address
        Address savedBusinessAddress = addressService.createAddress(businessAddress);

        // Create and save the service provider entity
        ServiceProvider serviceProvider = ServiceProviderMapper.toEntity(serviceProviderDto, savedBusinessAddress);
        serviceProvider.setCountry(CountryMapper.toEntity(CountryContextHolder.getCountry()));
        ServiceProvider savedServiceProvider = serviceProviderRepository.save(serviceProvider);

        // Save subscribed services
        saveSubscribedServicesOfTheServiceProvider(serviceProviderServices, savedServiceProvider);

        // Save tax info
        saveTaxInfoOfTheServiceProvider(taxInfo, savedServiceProvider);

        // Save agreements
        saveAgreementsOfTheServiceProvider(agreements, savedServiceProvider);

        // Register the logged-in user as employee with manager role
        linkLoggedInUserAsOwnerOfTheCreatedServiceProvider(loggedInUserId, savedServiceProvider);

        // Create service provider wallet for the service provider
        walletService.createWalletForServiceProvider(savedServiceProvider);

        return ServiceProviderMapper.toDto(savedServiceProvider);
    }

    private void saveSubscribedServicesOfTheServiceProvider(List<ServiceProviderServiceCreateDto> serviceProviderServices, ServiceProvider savedServiceProvider) {
        List<ServiceProviderService> providerServices = new ArrayList<>();
        if (serviceProviderServices != null && !serviceProviderServices.isEmpty()) {
            for (ServiceProviderServiceCreateDto serviceProviderServiceCreateDto : serviceProviderServices) {
                com.muyategna.backend.professional_service.entity.Service service = serviceService
                        .getServiceById(serviceProviderServiceCreateDto.getServiceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceProviderServiceCreateDto.getServiceId()));

                ServiceProviderService serviceProviderService = ServiceProviderServiceMapper.toEntity(
                        serviceProviderServiceCreateDto,
                        service,
                        savedServiceProvider);

                serviceProviderService.setIsActive(true);

                providerServices.add(serviceProviderService);

            }

            // Save all provider services in a single batch
            List<ServiceProviderService> savedProviderServices = serviceProviderServiceService.createServiceProviderServices(providerServices);
        }
    }

    private void saveTaxInfoOfTheServiceProvider(ServiceProviderTaxInfoCreateDto taxInfo, ServiceProvider savedServiceProvider) {
        if (taxInfo != null) {
            ServiceProviderTaxInfo savedTaxInfo = ServiceProviderTaxInfoMapper.toEntity(taxInfo, savedServiceProvider);
            ServiceProviderTaxInfo savedServiceProviderTaxInfo = serviceProviderTaxInfoService.saveTaxInfo(savedTaxInfo);
        }
    }

    private void saveAgreementsOfTheServiceProvider(List<ServiceProviderAgreementCreateDto> agreements, ServiceProvider savedServiceProvider) {
        if (agreements != null && !agreements.isEmpty()) {

            List<ServiceProviderAgreement> providerAgreements = new ArrayList<>();
            for (ServiceProviderAgreementCreateDto agreementDto : agreements) {
                if (agreementDto.getDocumentId() != null) {
                    LegalDocument legalDocument = legalDocumentService.getLegalDocumentById(agreementDto.getDocumentId());
                    ServiceProviderAgreement agreement = ServiceProviderAgreementMapper.toEntity(agreementDto, legalDocument, savedServiceProvider);
                    providerAgreements.add(agreement);
                }
            }

            List<ServiceProviderAgreement> savedAgreements = serviceProviderAgreementService.createServiceProviderAgreements(providerAgreements);

        }
    }

    private void linkLoggedInUserAsOwnerOfTheCreatedServiceProvider(UUID loggedInUserId, ServiceProvider savedServiceProvider) {
        // First get the service provider role "OWNER"
        ServiceProviderRole serviceProviderRole = serviceProviderRoleService
                .getServiceProviderRoleByRoleName("OWNER");

        // Find the logged-in user's profile
        UserProfile userProfile = userProfileRepository.findByKeycloakUserId(loggedInUserId).orElseThrow(() -> new ResourceNotFoundException("User profile not found with ID: " + loggedInUserId));

        // Create and save the service employee
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        serviceEmployee.setUserProfile(userProfile);
        serviceEmployee.setServiceProvider(savedServiceProvider);
        serviceEmployee.setIsActive(true);
        serviceEmployee.setIsBlocked(false);
        ServiceEmployee savedServiceEmployee = serviceEmployeeService.createServiceEmployee(serviceEmployee);

        // Assign the logged-in user as employee with manager role
        ServiceEmployeeRole serviceEmployeeRole = new ServiceEmployeeRole();
        serviceEmployeeRole.setRole(serviceProviderRole);
        serviceEmployeeRole.setEmployee(savedServiceEmployee);

        ServiceEmployeeRole savedServiceEmployeeRole =
                serviceEmployeeRoleService.createServiceEmployeeRole(serviceEmployeeRole);
    }


    //    @Transactional
    @Override
    public ServiceProviderDto updateServiceProviderById(Long serviceProviderId, ServiceProviderUpdateDto serviceProviderDto) {
        ServiceProvider existingServiceProvider = serviceProviderRepository
                .findById(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found with ID: " + serviceProviderId));

        if (existingServiceProvider.getSubscribedServices() != null) {
            existingServiceProvider.setSubscribedServices(new ArrayList<>(existingServiceProvider.getSubscribedServices()));
        }

        ServiceProvider updatedServiceProvider = ServiceProviderMapper.toEntity(serviceProviderDto, existingServiceProvider);
        ServiceProvider savedServiceProvider = serviceProviderRepository.save(updatedServiceProvider);

        return new ServiceProviderDto(); //ServiceProviderMapper.toDto(savedServiceProvider);
    }

    @Override
    public ServiceProviderDto getServiceProviderById(Long serviceProviderId) {
        log.info("Retrieving Service Provider with ID: {}", serviceProviderId);
        ServiceProvider serviceProvider = serviceProviderRepository
                .findById(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found with ID: " + serviceProviderId));
        log.info("Retrieved Service Provider: {}", serviceProvider);
        return ServiceProviderMapper.toDto(serviceProvider);
    }

    @Override
    public List<ServiceProviderDto> getServiceProviderServices(Long serviceProviderId) {
        return List.of();
    }

    @Transactional
    @Override
    public Boolean activateServiceProviderById(Long serviceProviderId) {
        log.info("Activating Service Provider with ID: {}", serviceProviderId);
        Integer rowsAffected = serviceProviderRepository.activateServiceProviderById(serviceProviderId);
        if (rowsAffected == 0) {
            log.error("Failed to activate Service Provider with ID: {}", serviceProviderId);
        }
        log.info("Service Provider activated successfully with ID: {}", serviceProviderId);
        return true;
    }


    @Transactional
    @Override
    public Boolean inActivateServiceProviderById(Long serviceProviderId) {
        log.info("inactivating Service Provider with ID: {}", serviceProviderId);
        Integer rowsAffected = serviceProviderRepository.inActivateServiceProviderById(serviceProviderId);
        if (rowsAffected == 0) {
            log.error("Failed to inactivate Service Provider with ID: {}", serviceProviderId);
        }
        log.info("Service Provider inactivated successfully with ID: {}", serviceProviderId);
        return true;
    }

    @Override
    public List<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> getServiceProviderServicesByServiceAndLocation(Long serviceId, String deliveryMode, String geoPointWkt, Double radius, Long currentCountryId, int queryLimit) {
        return serviceProviderServiceRepository.findServiceProviderServicesByServiceAndLocation(serviceId, deliveryMode, geoPointWkt, radius, currentCountryId, queryLimit);

    }

    @Override
    public Set<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> getServiceProviderServicesByServiceAndLocation(Long serviceId, String deliveryMode, Point geoPoint, double radius, List<Long> excludedServiceProviderIds, Long currentCountryId) {
        return serviceProviderServiceRepository.findServiceProviderServicesByServiceAndLocation(serviceId, deliveryMode, geoPoint, radius, excludedServiceProviderIds, currentCountryId);
    }

    @Override
    public List<ServiceProviderService> getServiceProviderServicesByProviderIdAndIsActive(Long serviceProviderId, boolean isActive) {
        log.info("Retrieving Service Provider");
        List<ServiceProviderService> serviceProviderServices = serviceProviderServiceRepository
                .findByProviderIdAndIsActive(serviceProviderId, isActive);
        log.info("Retrieved Service Provider services of size: {}", serviceProviderServices.size());
        return serviceProviderServices;
    }

    @Cacheable(value = CacheConstants.CACHE_SERVICE_PROVIDER,
            key = "T(com.muyategna.backend.redis.ServiceProviderCacheKeyUtil).getServiceProviderServiceKey(#serviceProviderId, #serviceId)")
    @Override
    public ServiceProviderServiceDto getServiceProviderServiceByProviderIdAndServiceId(Long serviceProviderId, Long serviceId) {
        log.info("Checking if Service Provider with ID: {} has subscribed to Service with ID: {}", serviceProviderId, serviceId);

        return serviceProviderServiceRepository
                .getServiceProviderServiceByProviderIdAndServiceIdAndIsActiveTrue(serviceProviderId, serviceId)
                .map(ServiceProviderServiceMapper::toDto)
                .orElseThrow(() -> new ServiceNotSubscribedException("Service Provider does not have the service with ID: " + serviceId));
    }

    @Override
    public List<ServiceProvider> findServiceProvidersByIdsWithNotificationPreferences(List<Long> serviceProviderIds) {
        log.info("Retrieving Service Providers with IDs: {}", serviceProviderIds);
        return serviceProviderRepository.findServiceProvidersByIdsWithNotificationPreferences(serviceProviderIds);
    }

    @Override
    public List<ProviderServiceStatProjection> getServiceProviderServicesByLocationAndService(Long serviceId, String deliveryMode, String jobRequestGeoPoint, double radiusMeters, Long countryId, int queryLimit) {
        log.info("Retrieving Service Providers by location and service");
        return serviceProviderServiceRepository.getServiceProviderServicesByLocationAndService(serviceId, deliveryMode, jobRequestGeoPoint, radiusMeters, countryId, queryLimit);
    }

    @Override
    public List<NearbyServiceProviderListDto> getServiceProviderList(List<Long> rankedServiceProviderIds) {
//        log.info("Retrieving service providers by ID: {}", rankedServiceProviderIds);
//        List<ServiceProvider> serviceProviders = serviceProviderRepository.findAllById(rankedServiceProviderIds);
//        log.info("Retrieved service providers: {}", serviceProviders);
//        return ServiceProviderMapper.toNearbyProviderListDto(serviceProviders);

        return List.of();
    }

    @Override
    public List<NearbyServiceProviderForAJobProjection> getNearbyServiceProvidersForCustomerView(List<Long> rankedServiceProviderIds, JobLead jobLead, String jobGeoPoint) {
        log.info("Retrieving service providers by ID: {}", rankedServiceProviderIds);
        List<NearbyServiceProviderForAJobProjection> serviceProvidersProjection = serviceProviderRepository.getNearbyServiceProvidersForCustomerView(rankedServiceProviderIds, jobLead.getId(), jobGeoPoint);
        log.info("Retrieved service providers projection");
        return serviceProvidersProjection;
    }

    @Override
    public boolean existsActiveServiceProviderServiceByProviderIdAndServiceId(Long serviceProviderId, Long serviceId) {
        log.info(" Checking if Service Provider with ID: {} has subscribed to Service with ID: {}", serviceProviderId, serviceId);
        return serviceProviderServiceRepository.existsByProviderIdAndServiceIdAndIsActiveTrue(serviceProviderId, serviceId);
    }

    @Override
    public Optional<ServiceProvider> getServiceProviderByIdWithNotificationPreference(Long providerId) {
        log.info("Retrieving Service Provider with ID: {} with notification preferences", providerId);
        return serviceProviderRepository.getServiceProviderByIdWithNotificationPreference(providerId);
    }
}
