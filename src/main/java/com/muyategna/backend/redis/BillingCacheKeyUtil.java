package com.muyategna.backend.redis;

public class BillingCacheKeyUtil {
    public static String getServiceCreditCostKey(Long serviceId) {
        return String.format("service_credit_cost_%d", serviceId);
    }

    public static String getServiceProviderSubscriptionKey(Long serviceProviderId) {
        return String.format("service_provider_subscription_%d", serviceProviderId);
    }

    public static String getSubscriptionPlanKey(Long subscriptionPlanId) {
        return String.format("subscription_plan_%d", subscriptionPlanId);
    }

    public static String getAddOnPlanKey(Long addOnPlanId) {
        return String.format("add_on_plan_%d", addOnPlanId);
    }

    public static String getPlatformBankInformationKey(Long countryId, boolean isActive) {
        return String.format("platform_bank_information_%d_%s", countryId, isActive ? "active" : "inactive");
    }

}