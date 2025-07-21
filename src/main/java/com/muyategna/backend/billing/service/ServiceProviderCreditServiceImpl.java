package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.entity.ServiceProviderCredit;
import com.muyategna.backend.billing.repository.ServiceProviderCreditRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceProviderCreditServiceImpl implements ServiceProviderCreditService {

    private final ServiceProviderCreditRepository serviceProviderCreditRepository;
    private final ServiceCreditCostService serviceCreditCostService;

    @Autowired
    public ServiceProviderCreditServiceImpl(ServiceProviderCreditRepository serviceProviderCreditRepository, ServiceCreditCostService serviceCreditCostService) {
        this.serviceProviderCreditRepository = serviceProviderCreditRepository;
        this.serviceCreditCostService = serviceCreditCostService;
    }

    @Override
    public boolean hasServiceProviderEnoughCredit(Long serviceProviderId, Long serviceId) {

        log.info("Checking if Service Provider with ID: {} has enough credits for Service with ID: {}", serviceProviderId, serviceId);
        // Retrieve service credit cost for the given service ID
        ServiceCreditCostDto serviceCreditCost = serviceCreditCostService.findByServiceId(serviceId);

        return serviceProviderCreditRepository
                        .hasServiceProviderEnoughCredit(serviceProviderId, serviceCreditCost.getPaygCreditsPerLead());
    }

    @Override
    public ServiceProviderCredit getServiceProviderCredit(Long serviceProviderId) {
        log.info("Retrieving ServiceProviderCredit for Service Provider with ID: {}", serviceProviderId);
        return serviceProviderCreditRepository.findByProviderId(serviceProviderId).
                orElseThrow(() -> new ResourceNotFoundException("Service Provider Credit not found for Service Provider ID: " + serviceProviderId));
    }

    @Override
    public void updateServiceProviderCredit(ServiceProviderCredit serviceProviderCredit) {
        log.info("Updating ServiceProviderCredit with ID: {}", serviceProviderCredit.getId());
        serviceProviderCreditRepository.save(serviceProviderCredit);
    }
}
