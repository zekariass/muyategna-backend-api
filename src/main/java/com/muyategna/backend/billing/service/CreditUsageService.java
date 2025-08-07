package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

import java.math.BigDecimal;

public interface CreditUsageService {
    void createCreditUsage(ServiceProvider serviceProviderId, JobLeadClaim savedJobLeadClaim, BigDecimal usedCreditAmount, CreditSource creditSource, Boolean isPartial);
}
