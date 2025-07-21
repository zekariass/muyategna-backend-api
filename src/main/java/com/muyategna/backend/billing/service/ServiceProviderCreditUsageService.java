package com.muyategna.backend.billing.service;

import com.muyategna.backend.job_request.entity.JobLeadClaim;

public interface ServiceProviderCreditUsageService {
    void createServiceProviderCreditUsage(Long serviceProviderId, JobLeadClaim savedJobLeadClaim, Long id);
}
