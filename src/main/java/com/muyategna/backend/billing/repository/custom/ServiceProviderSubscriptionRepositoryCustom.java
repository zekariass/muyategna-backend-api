package com.muyategna.backend.billing.repository.custom;

import java.math.BigDecimal;

public interface ServiceProviderSubscriptionRepositoryCustom {
    boolean hasServiceProviderValidSubscription(Long serviceProviderId, BigDecimal subsCreditsPerLead);
}
