package com.muyategna.backend.billing.strategies.credit_usage_strategy;

import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.job_request.exception.NoValidSubscriptionOrCreditFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditStrategyResolver {
    private final SubscriptionCreditStrategy subscriptionCreditStrategy;
    private final HybridCreditStrategy hybridCreditStrategy;
    private final PayGoCreditStrategy payGoCreditStrategy;

    @Autowired
    public CreditStrategyResolver(SubscriptionCreditStrategy subscriptionCreditStrategy, HybridCreditStrategy hybridCreditStrategy, PayGoCreditStrategy payGoCreditStrategy) {
        this.subscriptionCreditStrategy = subscriptionCreditStrategy;
        this.hybridCreditStrategy = hybridCreditStrategy;
        this.payGoCreditStrategy = payGoCreditStrategy;
    }

    public CreditUsageStrategy resolve(CreditContext context) {

        BigDecimal subsTotal = context.getCreditCost().getSubsClaimCostPerLead()
                .add(context.getCreditCost().getSubsAcceptanceCostPerLead());
        BigDecimal paygTotal = context.getCreditCost().getPaygClaimCostPerLead()
                .add(context.getCreditCost().getPaygAcceptanceCostPerLead());

        ServiceProviderSubscription subscription = context.getWallet().getSubscription();
        PayGoCredit payGoCredit = context.getWallet().getPaygoCredit();

        BigDecimal subsBalance = subscription != null && subscription.isValid() ? subscription.getBalance() : BigDecimal.ZERO;
        BigDecimal paygoBalance = payGoCredit != null ? payGoCredit.getBalance() : BigDecimal.ZERO;

        if (subsBalance.compareTo(subsTotal) >= 0) return subscriptionCreditStrategy;
        if (subsBalance.compareTo(BigDecimal.ZERO) > 0 && subsBalance.add(paygoBalance).compareTo(subsTotal) >= 0)
            return hybridCreditStrategy;
        if (paygoBalance.compareTo(paygTotal) >= 0) return payGoCreditStrategy;

        throw new NoValidSubscriptionOrCreditFoundException("Insufficient funds for claim.");


    }
}
