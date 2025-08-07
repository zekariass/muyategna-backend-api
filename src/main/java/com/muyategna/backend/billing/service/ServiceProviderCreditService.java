package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.PayGoCredit;

import java.math.BigDecimal;

public interface ServiceProviderCreditService {
    boolean hasServiceProviderEnoughCredit(Long serviceProviderId, Long serviceId, BigDecimal paygClaimCostPerLead);

    PayGoCredit getServiceProviderCredit(Long serviceProviderId);

    void updateServiceProviderCredit(PayGoCredit payGoCredit);
}
