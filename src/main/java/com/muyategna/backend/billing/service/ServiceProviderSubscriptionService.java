package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_provider_subscription.ServiceProviderSubscriptionDto;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

import java.math.BigDecimal;
import java.util.Optional;

public interface ServiceProviderSubscriptionService {
    boolean hasServiceProviderValidSubscription(Long serviceProviderId, Long serviceId, BigDecimal subsClaimCostPerLead);

    ServiceProviderSubscriptionDto getServiceProviderSubscription(Long serviceProviderId);

    Optional<ServiceProviderSubscription> getOptionalServiceProviderSubscription(Long serviceProviderId);

    void updateServiceProviderSubscription(ServiceProviderSubscription subscription);

    boolean hasValidSubscription(ServiceProviderSubscription subscription);

    void save(ServiceProviderSubscription serviceProviderSubscription);

    ServiceProviderSubscription createOrUpdateServiceProviderSubscription(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider);
}
