package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.ServiceProviderSubscriptionUsage;
import com.muyategna.backend.billing.repository.ServiceProviderSubscriptionRepository;
import com.muyategna.backend.billing.repository.ServiceProviderSubscriptionUsageRepository;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ServiceProviderSubscriptionUsageServiceImpl implements ServiceProviderSubscriptionUsageService {

    private final ServiceProviderSubscriptionUsageRepository serviceProviderSubscriptionUsageRepository;
    private final ServiceProviderSubscriptionService subscriptionService;
    private final ServiceProviderSubscriptionRepository serviceProviderSubscriptionRepository;
    private final ServiceCreditCostService serviceCreditCostService;

    @Autowired
    public ServiceProviderSubscriptionUsageServiceImpl(ServiceProviderSubscriptionUsageRepository serviceProviderSubscriptionUsageRepository, ServiceProviderSubscriptionService subscriptionService, ServiceProviderSubscriptionRepository serviceProviderSubscriptionRepository, ServiceCreditCostService serviceCreditCostService) {
        this.serviceProviderSubscriptionUsageRepository = serviceProviderSubscriptionUsageRepository;
        this.subscriptionService = subscriptionService;
        this.serviceProviderSubscriptionRepository = serviceProviderSubscriptionRepository;
        this.serviceCreditCostService = serviceCreditCostService;
    }


    @Override
    public void createServiceProviderSubscriptionUsage(Long serviceProviderId, JobLeadClaim savedJobLeadClaim, Long serviceId) {
        log.info("Creating ServiceProviderSubscriptionUsage for Service Provider with ID: {} and Job Lead Claim with ID: {}", serviceProviderId, savedJobLeadClaim.getId());
        // Get ServiceCreditCost for the service
        ServiceCreditCostDto serviceCreditCost = serviceCreditCostService.findByServiceId(serviceId);


        // Get Subscription object for the service provider
        ServiceProviderSubscription subscription = serviceProviderSubscriptionRepository.findByProviderIdAndIsActiveTrue(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider Subscription not found for Service Provider ID: " + serviceProviderId));

        // Create a new ServiceProviderSubscriptionUsage object
        ServiceProviderSubscriptionUsage serviceProviderSubscriptionUsage = new ServiceProviderSubscriptionUsage();
        serviceProviderSubscriptionUsage.setSubscription(subscription);
        serviceProviderSubscriptionUsage.setJobLeadClaim(savedJobLeadClaim);
        serviceProviderSubscriptionUsage.setUsedCredits(serviceCreditCost.getSubsCreditsPerLead());
        serviceProviderSubscriptionUsage.setUsedAt(savedJobLeadClaim.getCreatedAt());

        // Save the ServiceProviderSubscriptionUsage object
        serviceProviderSubscriptionUsageRepository.save(serviceProviderSubscriptionUsage);

        // Update the subscription's remaining credits
        BigDecimal newUsedCredits = subscription.getUsedCredits().add(serviceCreditCost.getSubsCreditsPerLead());
        subscription.setUsedCredits(newUsedCredits);
        if (newUsedCredits.compareTo(subscription.getInitialCredits()) >= 0) {
            // TODO: Notify the service provider that the subscription has reached its limit
        }
        subscriptionService.updateServiceProviderSubscription(subscription);
        log.info("ServiceProviderSubscriptionUsage created successfully for Service Provider with ID: {} and Job Lead Claim with ID: {}", serviceProviderId, savedJobLeadClaim.getId());
    }
}
