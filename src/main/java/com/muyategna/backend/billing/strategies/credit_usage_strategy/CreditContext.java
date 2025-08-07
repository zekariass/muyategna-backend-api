package com.muyategna.backend.billing.strategies.credit_usage_strategy;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.enums.CreditSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class CreditContext {
    private final ServiceCreditCostDto creditCost;
    private final ServiceProviderWallet wallet;
    private final Long serviceProviderId;
    private CreditSource creditSource;
    private BigDecimal usedCreditAmount;
}
