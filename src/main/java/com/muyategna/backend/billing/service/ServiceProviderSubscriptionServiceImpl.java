package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.dto.service_provider_subscription.ServiceProviderSubscriptionDto;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.mapper.ServiceProviderSubscriptionMapper;
import com.muyategna.backend.billing.repository.ServiceCreditCostRepository;
import com.muyategna.backend.billing.repository.ServiceProviderSubscriptionRepository;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ServiceProviderSubscriptionServiceImpl implements ServiceProviderSubscriptionService {
   private final ServiceProviderSubscriptionRepository subscriptionRepository;
   private final ServiceCreditCostService serviceCreditCostService;

   @Autowired
    public ServiceProviderSubscriptionServiceImpl(ServiceProviderSubscriptionRepository subscriptionRepository, ServiceCreditCostRepository serviceCreditCostRepository, ServiceCreditCostService serviceCreditCostService) {
        this.subscriptionRepository = subscriptionRepository;
       this.serviceCreditCostService = serviceCreditCostService;
   }

    @Override
    public boolean hasServiceProviderValidSubscription(Long serviceProviderId, Long serviceId) {
       log.info("Checking if Service Provider with ID: {} has a valid subscription for Service with ID: {}", serviceProviderId, serviceId);
       // Get the number of credits needed for the service
        ServiceCreditCostDto serviceCreditCost = serviceCreditCostService.findByServiceId(serviceId);

        // Check if the service provider has a valid subscription
       return subscriptionRepository.hasServiceProviderValidSubscription(serviceProviderId, serviceCreditCost.getSubsCreditsPerLead());
    }

    @Cacheable(value = CacheConstants.CACHE_BILLING,
            key = "T(com.muyategna.backend.redis.BillingCacheKeyUtil).getServiceProviderSubscriptionKey(#serviceProviderId)")
    @Override
    public ServiceProviderSubscriptionDto getServiceProviderSubscription(Long serviceProviderId) {
        log.info("Retrieving Service Provider Subscription for Service Provider with ID: {}", serviceProviderId);
        return subscriptionRepository.findByProviderIdAndIsActiveTrue(serviceProviderId)
                .map(ServiceProviderSubscriptionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider Subscription not found for Service Provider ID: " + serviceProviderId));
   }


    // This method retrieves an Optional<> ServiceProviderSubscription for a given service provider ID.
    @Cacheable(value = CacheConstants.CACHE_BILLING,
            key = "T(com.muyategna.backend.redis.BillingCacheKeyUtil).getServiceProviderSubscriptionKey(#serviceProviderId)")
    @Override
    public Optional<ServiceProviderSubscription> getOptionalServiceProviderSubscription(Long serviceProviderId) {
        log.info("Getting Service Provider Subscription for Service Provider with ID: {}", serviceProviderId);
        return subscriptionRepository.findByProviderIdAndIsActiveTrue(serviceProviderId);
    }

    @Override
    public void updateServiceProviderSubscription(ServiceProviderSubscription subscription) {
        log.info("Updating Service Provider Subscription for Service Provider with ID: {}", subscription.getProvider().getId());
        subscriptionRepository.save(subscription);
    }
}
