package com.muyategna.backend.professional_service.service;

import com.muyategna.backend.professional_service.dto.availability.ServiceCountryAvailabilityDto;
import com.muyategna.backend.professional_service.mapper.ServiceCountryAvailabilityMapper;
import com.muyategna.backend.professional_service.repository.ServiceCountryAvailabilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServiceCountryAvailabilityServiceImpl implements ServiceCountryAvailabilityService {

    private final ServiceCountryAvailabilityRepository serviceCountryAvailabilityRepository;

    @Autowired
    public ServiceCountryAvailabilityServiceImpl(ServiceCountryAvailabilityRepository serviceCountryAvailabilityRepository) {
        this.serviceCountryAvailabilityRepository = serviceCountryAvailabilityRepository;
    }

    @Override
    public List<ServiceCountryAvailabilityDto> getActiveAvailableServicesForCountry(Long countryId) {
        log.info("Retrieving active available services for country: {}", countryId);
        List<ServiceCountryAvailabilityDto> availableServices = serviceCountryAvailabilityRepository.findByCountryIdAndIsActiveTrue(countryId)
                .stream()
                .map(ServiceCountryAvailabilityMapper::toDto)
                .toList();

        log.info("Retrieved {} active available services for country: {}", availableServices.size(), countryId);
        return availableServices;
    }

    @Override
    public boolean isActiveServiceExistsForCountry(Long serviceId, Long countryId) {
        log.info("Checking if active service exists for serviceId: {} and countryId: {}", serviceId, countryId);

        return serviceCountryAvailabilityRepository.existsByServiceIdAndCountryIdAndIsActiveTrue(serviceId, countryId);
    }

    @Override
    public List<ServiceCountryAvailabilityDto> getActiveAvailableServicesForCountryByName(String serviceName, Long countryId) {
        log.info("Retrieving active available services for country: {} by service name: {}", countryId, serviceName);
        List<ServiceCountryAvailabilityDto> availableServices = serviceCountryAvailabilityRepository.findByCountryIdAndIsActiveAndServiceName(countryId, true, serviceName)
                .stream()
                .map(ServiceCountryAvailabilityMapper::toDto)
                .toList();
        log.info("Retrieved {} active available services for country: {} by service name: {}", availableServices.size(), countryId, serviceName);
        return availableServices;
    }

    @Override
    public List<ServiceCountryAvailabilityDto> getActiveAvailableServicesForCountryByCategory(Long categoryId, Long countryId) {
        log.info("Retrieving active available services for country: {} by service category ID: {}", countryId, categoryId);
        List<ServiceCountryAvailabilityDto> availableServices = serviceCountryAvailabilityRepository.findByCountryIdAndIsActiveAndCategoryId(countryId, true, categoryId)
                .stream()
                .map(ServiceCountryAvailabilityMapper::toDto)
                .toList();
        log.info("Retrieved {} active available services for country: {} by service category ID: {}", availableServices.size(), countryId, categoryId);
        return availableServices;
    }
}
