package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.entity.ServiceProviderCredit;
import com.muyategna.backend.billing.entity.ServiceProviderCreditUsage;
import com.muyategna.backend.billing.repository.ServiceProviderCreditUsageRepository;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ServiceProviderCreditUsageServiceImpl implements ServiceProviderCreditUsageService {

    private final ServiceProviderCreditUsageRepository serviceProviderCreditUsageRepository;
    private final ServiceCreditCostService serviceCreditCostService;
    private final ServiceProviderCreditService serviceProviderCreditService;

    @Autowired
    public ServiceProviderCreditUsageServiceImpl(ServiceProviderCreditUsageRepository serviceProviderCreditUsageRepository, ServiceCreditCostService serviceCreditCostService, ServiceProviderCreditService serviceProviderCreditService) {
        this.serviceProviderCreditUsageRepository = serviceProviderCreditUsageRepository;
        this.serviceCreditCostService = serviceCreditCostService;
        this.serviceProviderCreditService = serviceProviderCreditService;
    }

    @Override
    public void createServiceProviderCreditUsage(Long serviceProviderId, JobLeadClaim savedJobLeadClaim, Long serviceId) {
        // Set up Service Provider object
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderId);

        // Retrieve the service credit cost for the service
        ServiceCreditCostDto serviceCreditCost = serviceCreditCostService.findByServiceId(serviceId);


        // Create a new ServiceProviderCreditUsage object
        ServiceProviderCreditUsage serviceProviderCreditUsage = new ServiceProviderCreditUsage();
        serviceProviderCreditUsage.setProvider(serviceProvider);
        serviceProviderCreditUsage.setJobLeadClaim(savedJobLeadClaim);
        serviceProviderCreditUsage.setUsedCreditAmount(serviceCreditCost.getPaygCreditsPerLead());
        serviceProviderCreditUsage.setUsedAt(savedJobLeadClaim.getCreatedAt());

        // Save the ServiceProviderCreditUsage object to the repository
        serviceProviderCreditUsageRepository.save(serviceProviderCreditUsage);

        // Update the service provider's credit balance
        // Get Service Provider Credits object for the service provider
        ServiceProviderCredit serviceProviderCredit = serviceProviderCreditService.getServiceProviderCredit(serviceProviderId);

        BigDecimal balance = serviceProviderCredit.getBalance().subtract(serviceCreditCost.getPaygCreditsPerLead());

        serviceProviderCredit.setBalance(balance);
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            // TODO: Notify the service provider that their credit balance is low or has reached zero
        }
        serviceProviderCredit.setLastUsedAt(savedJobLeadClaim.getCreatedAt());

        serviceProviderCreditService.updateServiceProviderCredit(serviceProviderCredit);

    }
}
