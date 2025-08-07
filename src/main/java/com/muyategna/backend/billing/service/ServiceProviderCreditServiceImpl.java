package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.repository.ServiceProviderCreditRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    public boolean hasServiceProviderEnoughCredit(Long serviceProviderId, Long serviceId, BigDecimal paygClaimCostPerLead) {

        log.info("Checking if Service Provider with ID: {} has enough credits for Service with ID: {}", serviceProviderId, serviceId);
//        // Retrieve service credit cost for the given service ID
//        ServiceCreditCostDto serviceCreditCost = serviceCreditCostService.findByServiceId(serviceId);

        return serviceProviderCreditRepository
                .hasServiceProviderEnoughCredit(serviceProviderId, paygClaimCostPerLead);
    }

    @Override
    public PayGoCredit getServiceProviderCredit(Long serviceProviderId) {
        log.info("Retrieving PayGoCredit for Service Provider with ID: {}", serviceProviderId);
        return serviceProviderCreditRepository.findByProvider_id(serviceProviderId).
                orElseThrow(() -> new ResourceNotFoundException("Service Provider Credit not found for Service Provider ID: " + serviceProviderId));
    }

    @Override
    public void updateServiceProviderCredit(PayGoCredit payGoCredit) {
        log.info("Updating PayGoCredit with ID: {}", payGoCredit.getId());
        serviceProviderCreditRepository.save(payGoCredit);
    }
}
