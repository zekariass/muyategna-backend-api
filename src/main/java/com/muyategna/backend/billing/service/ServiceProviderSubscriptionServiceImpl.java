package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_provider_subscription.ServiceProviderSubscriptionDto;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.SubscriptionPlan;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.RenewalStatusEnum;
import com.muyategna.backend.billing.mapper.ServiceProviderSubscriptionMapper;
import com.muyategna.backend.billing.mapper.SubscriptionPlanMapper;
import com.muyategna.backend.billing.repository.ServiceCreditCostRepository;
import com.muyategna.backend.billing.repository.ServiceProviderSubscriptionRepository;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ServiceProviderSubscriptionServiceImpl implements ServiceProviderSubscriptionService {
    private final ServiceProviderSubscriptionRepository subscriptionRepository;
    private final ServiceCreditCostService serviceCreditCostService;
    private final SubscriptionPlanService subscriptionPlanService;
    private final SystemConfigService systemConfigService;

    @Autowired
    public ServiceProviderSubscriptionServiceImpl(ServiceProviderSubscriptionRepository subscriptionRepository, ServiceCreditCostRepository serviceCreditCostRepository, ServiceCreditCostService serviceCreditCostService, SubscriptionPlanService subscriptionPlanService, SystemConfigService systemConfigService) {
        this.subscriptionRepository = subscriptionRepository;
        this.serviceCreditCostService = serviceCreditCostService;
        this.subscriptionPlanService = subscriptionPlanService;
        this.systemConfigService = systemConfigService;
    }


    @Override
    public void save(ServiceProviderSubscription serviceProviderSubscription) {
        log.info("Saving Service Provider Subscription for Service Provider");
        subscriptionRepository.save(serviceProviderSubscription);
    }

    @Override
    public ServiceProviderSubscription createOrUpdateServiceProviderSubscription(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        log.info("Creating or updating Service Provider Subscription for Service Provider with ID: {}", serviceProvider.getId());

        Long subscriptionPlanId = invoice.getProductPlanId();
        if (subscriptionPlanId == null) {
            log.warn("Invoice {} has no productPlanId (subscriptionPlanId), skipping Service Provider Subscription creation or update.", invoice.getId());
            return null;
        }

        // Get SubscriptionPlan
        SubscriptionPlan subscriptionPlan = SubscriptionPlanMapper.toEntity(subscriptionPlanService.getSubscriptionPlan(subscriptionPlanId));
        if (subscriptionPlan == null) {
            log.warn("No SubscriptionPlan found for productPlanId: {} (Invoice {})", subscriptionPlanId, invoice.getId());
            return null;
        }

        // Retrieve ServiceProviderSubscription
        ServiceProviderSubscription subscription = subscriptionRepository.findByProviderIdAndIsActiveTrue(serviceProvider.getId()).orElse(null);

        if (subscription == null) {
            log.info("No active Service Provider Subscription found for Service Provider with ID: {}. Creating a new subscription.", serviceProvider.getId());
            subscription = createServiceProviderSubscription(invoice, updatedTransaction, serviceProvider, subscriptionPlan);

        } else {
            log.info("Updating existing Service Provider Subscription for Service Provider with ID: {}", serviceProvider.getId());
            setCommonValues(subscription, subscriptionPlan);
        }

        return subscriptionRepository.save(subscription);
    }

    private static void setCommonValues(ServiceProviderSubscription subscription, SubscriptionPlan subscriptionPlan) {
        subscription.setInitialCredits(subscriptionPlan.getCreditsIncluded());
        subscription.setBalance(subscriptionPlan.getCreditsIncluded());

        int billingCycle = subscriptionPlan.getBillingCycle().getDays();
        subscription.setEndDate(subscription.getEndDate().plusDays((long) billingCycle * subscriptionPlan.getBillingCycleCount()));
        subscription.setIsActive(true);
        subscription.setLastBillingDate(LocalDateTime.now());
        subscription.setNextBillingDate(subscription.getEndDate());
        subscription.setRenewalAttemptedAt(LocalDateTime.now());
        subscription.setRenewalStatus(RenewalStatusEnum.SUCCESSFUL);
    }

    private ServiceProviderSubscription createServiceProviderSubscription(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider, SubscriptionPlan subscriptionPlan) {
        ServiceProviderSubscription subscription = new ServiceProviderSubscription();
        subscription.setStartedAt(LocalDateTime.now());
        subscription.setProvider(serviceProvider);

        setCommonValues(subscription, subscriptionPlan);
        subscription.setSubscriptionPlan(subscriptionPlan);
        subscription.setExtraData(null);

        subscription.setAutoRenew(false);
        subscription.setExtraData(null);

        Integer gracePeriodDays = systemConfigService
                .getSystemConfigByNameOptional("SUBSCRIPTION_GRACE_PERIOD_DAYS")
                .map(config -> {
                    try {
                        return Integer.parseInt(config.getValue());
                    } catch (NumberFormatException e) {
                        log.warn("Invalid number format for grace period days: {}", config.getValue());
                        return 0;
                    }
                })
                .orElse(0);

        subscription.setGracePeriodStartedAt(LocalDateTime.now());
        subscription.setGracePeriodEndsAt(subscription.getGracePeriodStartedAt().plusDays(gracePeriodDays));

        log.info("Created new Service Provider Subscription for Service Provider with ID: {}", serviceProvider.getId());
        return subscription;
    }

    @Override
    public boolean hasServiceProviderValidSubscription(Long serviceProviderId, Long serviceId, BigDecimal subsClaimCostPerLead) {
        log.info("Checking if Service Provider with ID: {} has a valid subscription for Service with ID: {}", serviceProviderId, serviceId);

        // Check if the service provider has a valid subscription
        return subscriptionRepository.hasServiceProviderValidSubscription(serviceProviderId, subsClaimCostPerLead);
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

    @Override
    public boolean hasValidSubscription(ServiceProviderSubscription subscription) {

        return false;
    }
}
