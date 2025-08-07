package com.muyategna.backend.billing.strategies.credit_usage_strategy;

import com.muyategna.backend.billing.entity.HoldCredit;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.enums.CreditSource;

import java.math.BigDecimal;

public interface CreditUsageStrategy {
    HoldCredit useCredit(CreditContext context);

    void applyClaimCost(ServiceProviderWallet serviceProviderWallet, BigDecimal claimCost);

    void applyAcceptanceCost(ServiceProviderWallet serviceProviderWallet, BigDecimal acceptanceCost);

    HoldCredit createHoldCredit(ServiceProviderWallet serviceProviderWallet, BigDecimal acceptanceCost, CreditSource creditSource);

    CreditSource getCreditSource();
}
