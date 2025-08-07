package com.muyategna.backend.billing.strategies.credit_usage_strategy;

import com.muyategna.backend.billing.entity.HoldCredit;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.billing.service.ServiceProviderSubscriptionService;
import com.muyategna.backend.billing.service.ServiceProviderWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SubscriptionCreditStrategy implements CreditUsageStrategy {

    private final ServiceProviderWalletService walletService;
    private final ServiceProviderSubscriptionService serviceProviderSubscriptionService;

    @Autowired
    public SubscriptionCreditStrategy(ServiceProviderWalletService walletService, ServiceProviderSubscriptionService serviceProviderSubscriptionService) {
        this.walletService = walletService;
        this.serviceProviderSubscriptionService = serviceProviderSubscriptionService;
    }

    @Override
    public HoldCredit useCredit(CreditContext context) {
        BigDecimal claimCost = context.getCreditCost().getSubsClaimCostPerLead();
        BigDecimal acceptanceCost = context.getCreditCost().getSubsAcceptanceCostPerLead();

        HoldCredit holdCredit = null;

        if (acceptanceCost.compareTo(BigDecimal.ZERO) > 0) {

            // Just hold the acceptance cost in wallet, do not deduct it from credit or subscription balance.
            applyAcceptanceCost(context.getWallet(), acceptanceCost);

            // Create a new Hold Credit
            holdCredit = createHoldCredit(context.getWallet(), acceptanceCost, CreditSource.SUBSCRIPTION);
        }

        applyClaimCost(context.getWallet(), claimCost);

        // Update the wallet
        walletService.save(context.getWallet());

        // Update the subscription
        updateSubscriptionBalance(context.getWallet().getSubscription(), claimCost);

        // Set the currently used credit source
        context.setCreditSource(getCreditSource());

        // Set the currently used credit amount at this stage which is the claim cost
        // The acceptance cost will be deducted after the lead claim is accepted
        context.setUsedCreditAmount(claimCost);

        return holdCredit;
    }

    private void updateSubscriptionBalance(ServiceProviderSubscription subscription, BigDecimal claimCost) {
        subscription.setBalance(subscription.getBalance().subtract(claimCost));
        serviceProviderSubscriptionService.save(subscription);
    }

    @Override
    public void applyClaimCost(ServiceProviderWallet serviceProviderWallet, BigDecimal claimCost) {
        serviceProviderWallet.setTotalCredit(serviceProviderWallet.getTotalCredit().subtract(claimCost));
        serviceProviderWallet.setAvailableCredit(serviceProviderWallet.getAvailableCredit().subtract(claimCost));
    }

    @Override
    public void applyAcceptanceCost(ServiceProviderWallet serviceProviderWallet, BigDecimal acceptanceCost) {
        serviceProviderWallet.setAvailableCredit(serviceProviderWallet.getAvailableCredit().subtract(acceptanceCost));
        serviceProviderWallet.setTotalHoldCredit(serviceProviderWallet.getTotalHoldCredit().add(acceptanceCost));
    }

    @Override
    public HoldCredit createHoldCredit(ServiceProviderWallet serviceProviderWallet, BigDecimal acceptanceCost, CreditSource creditSource) {
        return new HoldCredit(serviceProviderWallet, acceptanceCost, creditSource);
    }

    @Override
    public CreditSource getCreditSource() {
        return CreditSource.SUBSCRIPTION;
    }
}
