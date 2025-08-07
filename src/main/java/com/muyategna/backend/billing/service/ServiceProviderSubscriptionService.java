package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_provider_subscription.ServiceProviderSubscriptionDto;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;

import java.math.BigDecimal;
import java.util.Optional;

public interface ServiceProviderSubscriptionService {
    boolean hasServiceProviderValidSubscription(Long serviceProviderId, Long serviceId, BigDecimal subsClaimCostPerLead);

    ServiceProviderSubscriptionDto getServiceProviderSubscription(Long serviceProviderId);

    Optional<ServiceProviderSubscription> getOptionalServiceProviderSubscription(Long serviceProviderId);

    void updateServiceProviderSubscription(ServiceProviderSubscription subscription);

    boolean hasValidSubscription(ServiceProviderSubscription subscription);

    void save(ServiceProviderSubscription serviceProviderSubscription);
}
