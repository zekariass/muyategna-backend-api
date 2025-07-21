package com.muyategna.backend.redis;

public class BillingCacheKeyUtil {
    public static String getServiceCreditCostKey(Long serviceId) {
        return String.format("service_credit_cost_%d", serviceId);
    }

    public static String getServiceProviderSubscriptionKey(Long serviceProviderId) {
        return String.format("service_provider_subscription_%d", serviceProviderId);
    }

}