package com.muyategna.backend.redis;

public class ServiceProviderCacheKeyUtil {
    public static String getServiceProviderServiceKey(Long serviceProviderId, Long serviceId) {
        return String.format("service_provider_service_%d_%d", serviceProviderId, serviceId);
    }

}
