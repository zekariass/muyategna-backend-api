package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderTaxInfo;
import com.muyategna.backend.service_provider.mapper.ServiceProviderTaxInfoMapper;
import com.muyategna.backend.service_provider.repository.ServiceProviderTaxInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceProviderTaxInfoServiceImpl implements ServiceProviderTaxInfoService {

    private final ServiceProviderTaxInfoRepository serviceProviderTaxInfoRepository;

    @Autowired
    public ServiceProviderTaxInfoServiceImpl(ServiceProviderTaxInfoRepository serviceProviderTaxInfoRepository) {
        this.serviceProviderTaxInfoRepository = serviceProviderTaxInfoRepository;
    }


    /**
     * Saves the tax information for a service provider.
     *
     * @param savedTaxInfo The ServiceProviderTaxInfo entity to save.
     * @return The saved ServiceProviderTaxInfo entity.
     */
    @Override
    public ServiceProviderTaxInfo saveTaxInfo(ServiceProviderTaxInfo savedTaxInfo) {
        if (savedTaxInfo == null) {
            throw new IllegalArgumentException("Tax info cannot be null");
        }

        log.info("Saving tax info");
        ServiceProviderTaxInfo saved = serviceProviderTaxInfoRepository.save(savedTaxInfo);
        log.info("Saved tax info with ID: {}", savedTaxInfo.getId());
        return saved;
    }


    /**
     * Retrieves the tax information for a service provider by its ID.
     *
     * @param serviceProviderTaxInfoId The ID of the service provider tax info.
     * @return The ServiceProviderTaxInfoDto containing the tax info.
     */
    @Override
    public ServiceProviderTaxInfoDto getServiceProviderTaxInfoAndServiceProviderId(Long serviceProviderTaxInfoId, Long serviceProviderId) {
        log.info("Retrieving tax info for service provider with ID: {}", serviceProviderTaxInfoId);
        ServiceProviderTaxInfo taxInfo = serviceProviderTaxInfoRepository.findByIdAndServiceProviderId(serviceProviderTaxInfoId, serviceProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Service Provider Tax Info not found with ID: " + serviceProviderTaxInfoId));
        log.info("Found tax info for service provider with ID: {}", serviceProviderTaxInfoId);
        return ServiceProviderTaxInfoMapper.toDto(taxInfo);
    }


    /**
     * Updates the tax information for a service provider.
     *
     * @param serviceProviderId         The ID of the service provider.
     * @param serviceProviderTaxInfoId  The ID of the tax info to update.
     * @param serviceProviderTaxInfoDto The DTO containing the updated tax info.
     * @return The updated ServiceProviderTaxInfoDto.
     */
    @Override
    public ServiceProviderTaxInfoDto updateServiceProviderTaxInfo(Long serviceProviderId,
                                                                  Long serviceProviderTaxInfoId,
                                                                  ServiceProviderTaxInfoUpdateDto serviceProviderTaxInfoDto) {

        log.info("Updating tax info for service provider with ID: {}", serviceProviderId);
        ServiceProviderTaxInfo taxInfo = serviceProviderTaxInfoRepository
                .findByIdAndServiceProviderId(serviceProviderTaxInfoId, serviceProviderId)
                .orElseThrow(() -> new IllegalArgumentException("Service Provider Tax Info not found with ID: " + serviceProviderTaxInfoId + " for Service Provider ID: " + serviceProviderId));

        ServiceProviderTaxInfo updatedTaxInfoData = ServiceProviderTaxInfoMapper.toEntity(serviceProviderTaxInfoDto, taxInfo.getServiceProvider());
        updatedTaxInfoData.setId(taxInfo.getId());

        log.info("Updated tax info for service provider with ID: {}", serviceProviderId);
        ServiceProviderTaxInfo updatedTaxInfo = serviceProviderTaxInfoRepository.save(updatedTaxInfoData);
        return ServiceProviderTaxInfoMapper.toDto(updatedTaxInfo);
    }
}
