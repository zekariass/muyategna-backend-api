package com.muyategna.backend.billing.service;

import com.muyategna.backend.job_request.entity.JobLeadClaim;

public interface ServiceProviderSubscriptionUsageService {
    void createServiceProviderSubscriptionUsage(Long serviceProviderId, JobLeadClaim savedJobLeadClaim, Long serviceId);
}
