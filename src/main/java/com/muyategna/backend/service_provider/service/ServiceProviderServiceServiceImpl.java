package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.ServiceCountryAvailability;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.mapper.ServiceMapper;
import com.muyategna.backend.professional_service.repository.ServiceCountryAvailabilityRepository;
import com.muyategna.backend.professional_service.repository.ServiceRepository;
import com.muyategna.backend.professional_service.service.ServiceTranslationService;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.mapper.ServiceProviderServiceMapper;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.service_provider.repository.ServiceProviderServiceRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceProviderServiceServiceImpl implements ServiceProviderServiceService {

    private final ServiceProviderServiceRepository serviceProviderServiceRepository;
    private final ServiceTranslationService serviceTranslationService;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceCountryAvailabilityRepository serviceCountryAvailabilityRepository;

    @Autowired
    public ServiceProviderServiceServiceImpl(ServiceProviderServiceRepository serviceProviderServiceRepository,
                                             ServiceTranslationService serviceTranslationService,
                                             ServiceProviderRepository serviceProviderRepository,
                                             ServiceRepository serviceRepository,
                                             ServiceCountryAvailabilityRepository serviceCountryAvailabilityRepository) {
        this.serviceProviderServiceRepository = serviceProviderServiceRepository;
        this.serviceTranslationService = serviceTranslationService;
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceRepository = serviceRepository;
        this.serviceCountryAvailabilityRepository = serviceCountryAvailabilityRepository;
    }


    @Override
    public List<ServiceProviderService> createServiceProviderServices(List<ServiceProviderService> providerServices) {
        log.info("Saving multiple provider services");
        List<ServiceProviderService> savedProviderServices = serviceProviderServiceRepository.saveAll(providerServices);
        log.info("Saved {} provider services", savedProviderServices.size());
        return savedProviderServices;
    }


    /**
     * Retrieves localized service provider services for a given service provider ID.
     *
     * @param serviceProviderId the ID of the service provider
     * @return a list of localized service provider service DTOs
     */
    @Transactional(readOnly = true)
    @Override
    public List<ServiceProviderServiceLocalizedDto> getLocalizedServiceProviderServices(Long serviceProviderId) {
        log.info("Retrieving services for service provider with ID: {}", serviceProviderId);

        // Get services for the given service provider ID
        List<ServiceProviderService> providerServices = serviceProviderServiceRepository.findByProviderId(serviceProviderId);

        // Get all services associated with the provider services
        List<com.muyategna.backend.professional_service.entity.Service> services = providerServices.stream()
                .map(ServiceProviderService::getService)
                .toList();

        // Create a map of services by ID for quick lookup
        Map<Long, com.muyategna.backend.professional_service.entity.Service> serviceMap = services.stream()
                .collect(Collectors
                        .toMap(com.muyategna.backend.professional_service.entity.Service::getId, Function.identity()));

        // Fetch localized service DTOs for the services
        List<ServiceLocalizedDto> serviceLocalizedDtoList =
                serviceTranslationService
                        .getServiceLocalizedDtoList(ServiceMapper.toDtoList(services));

        // Create a map of localized service DTOs by service ID for quick lookup
        Map<Long, ServiceLocalizedDto> serviceLocalizedDtoMap = serviceLocalizedDtoList.stream()
                .collect(Collectors
                        .toMap(ServiceLocalizedDto::getId, Function.identity()));

        // Map provider services to localized DTOs using the service localized DTO map
        List<ServiceProviderServiceLocalizedDto> serviceProviderServiceLocalizedDtoList = providerServices.stream()
                .map(providerService -> ServiceProviderServiceMapper.toLocalizedDto(providerService, serviceLocalizedDtoMap.get(providerService.getService().getId())))
                .toList();

        log.info("Retrieved {} provider services", serviceProviderServiceLocalizedDtoList.size());

        return serviceProviderServiceLocalizedDtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceProviderServiceLocalizedDto getLocalizedServiceProviderServiceByIdAndServiceProviderId(Long serviceProviderServiceId, Long serviceProviderId) {
        log.info("Retrieving localized service provider service with ID: {}", serviceProviderServiceId);
        ServiceProviderService providerService = serviceProviderServiceRepository
                .findByIdAndProviderId(serviceProviderServiceId, serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider service not found with ID: " + serviceProviderServiceId));

        ServiceProviderServiceLocalizedDto serviceProviderServiceLocalizedDto = localizeServiceProviderService(providerService);

        log.info("Retrieved localized service provider service with ID: {}", serviceProviderServiceId);
        return serviceProviderServiceLocalizedDto;
    }

    private ServiceProviderServiceLocalizedDto localizeServiceProviderService(ServiceProviderService providerService) {
        // Fetch the service translation for the service associated with the provider service
        ServiceTranslation serviceTranslation = serviceTranslationService.getTranslationByServiceId(providerService.getService().getId());

        // Convert the service and service translation to localized DTOs
        ServiceLocalizedDto serviceLocalizedDto = ServiceMapper.toLocalizedDto(ServiceMapper.toDto(providerService.getService()), serviceTranslation);

        // Convert the provider service to a localized DTO using the localized service DTO
        return ServiceProviderServiceMapper.toLocalizedDto(providerService, serviceLocalizedDto);
    }


    @Override
    public ServiceProviderServiceLocalizedDto activateServiceProviderService(Long serviceProviderServiceId, Long serviceProviderId) {
        log.info("Activating service provider service with ID: {}", serviceProviderServiceId);
        ServiceProviderService providerService = serviceProviderServiceRepository
                .findById(serviceProviderServiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider service not found with ID: " + serviceProviderServiceId));

        // Check if the service provider ID matches the one associated with the provider service
        if (!Objects.equals(providerService.getProvider().getId(), serviceProviderId)) {
            throw new IllegalArgumentException("Service provider ID does not match the one associated with the service provider service.");
        }

        providerService.setIsActive(true);
        ServiceProviderService savedProviderService = serviceProviderServiceRepository.save(providerService);
        log.info("Activated service provider service with ID: {}", serviceProviderServiceId);

        return localizeServiceProviderService(savedProviderService);
    }


    @Override
    public ServiceProviderServiceLocalizedDto inactivateServiceProviderService(Long serviceProviderServiceId, Long serviceProviderId) {
        log.info("Inactivating service provider service with ID: {}", serviceProviderServiceId);
        ServiceProviderService providerService = serviceProviderServiceRepository
                .findById(serviceProviderServiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider service not found with ID: " + serviceProviderServiceId));

        // Check if the service provider ID matches the one associated with the provider service
        if (!Objects.equals(providerService.getProvider().getId(), serviceProviderId)) {
            throw new IllegalArgumentException("Service provider ID does not match the one associated with the service provider service.");
        }

        providerService.setIsActive(false);
        ServiceProviderService savedProviderService = serviceProviderServiceRepository.save(providerService);
        log.info("inactivated service provider service with ID: {}", serviceProviderServiceId);

        return localizeServiceProviderService(savedProviderService);
    }


    @Override
    public ServiceProviderServiceLocalizedDto createServiceProviderService(Long serviceProviderId,
                                                                           ServiceProviderServiceCreateDto serviceProviderServiceDto,
                                                                           Country country) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(() -> new ResourceNotFoundException("Service provider not found with ID: " + serviceProviderId));

        if (!Objects.equals(provider.getCountry().getId(), country.getId())) {
            throw new IllegalArgumentException("Service provider country does not match the provided country.");
        }

        // Check if a service country availability exists for the service and country
        ServiceCountryAvailability serviceCountryAvailability = serviceCountryAvailabilityRepository
                .findByServiceIdAndCountryIdAndIsActiveTrue(
                        serviceProviderServiceDto.getServiceId(), country.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service country availability not found for service ID: " + serviceProviderServiceDto.getServiceId() + " and country ID: " + country.getId()));

        com.muyategna.backend.professional_service.entity.Service service = serviceRepository.findById(serviceProviderServiceDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceProviderServiceDto.getServiceId()));

        ServiceProviderService providerService = ServiceProviderServiceMapper.toEntity(serviceProviderServiceDto, service, provider);
        ServiceProviderService savedProviderService = serviceProviderServiceRepository.save(providerService);

        return localizeServiceProviderService(savedProviderService);
    }
}
