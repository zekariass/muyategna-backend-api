package com.muyategna.backend.billing.strategies.credit_usage_strategy;

import com.muyategna.backend.billing.entity.HoldCredit;
import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.billing.service.PayGoCreditService;
import com.muyategna.backend.billing.service.ServiceProviderSubscriptionService;
import com.muyategna.backend.billing.service.ServiceProviderWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HybridCreditStrategy implements CreditUsageStrategy {

    private final ServiceProviderWalletService serviceProviderWalletService;
    private final ServiceProviderSubscriptionService subscriptionService;
    private final PayGoCreditService payGoCreditService;

    private BigDecimal claimCost = BigDecimal.ZERO;

    @Autowired
    public HybridCreditStrategy(ServiceProviderWalletService walletService, ServiceProviderSubscriptionService subscriptionService, PayGoCreditService payGoCreditService) {
        this.serviceProviderWalletService = walletService;
        this.subscriptionService = subscriptionService;
        this.payGoCreditService = payGoCreditService;
    }


    @Override
    public HoldCredit useCredit(CreditContext context) {
        BigDecimal subsClaimCost = context.getCreditCost().getSubsClaimCostPerLead();
        BigDecimal subsAcceptanceCost = context.getCreditCost().getSubsAcceptanceCostPerLead();

        BigDecimal subsBalance = context.getWallet().getSubscription().getBalance();

        HoldCredit holdCredit = null;

        if (subsAcceptanceCost.compareTo(BigDecimal.ZERO) > 0) {
            // BigDecimal fromSubs = subsBalance.min(subsAcceptanceCost);
            // BigDecimal fromPaygo = subsAcceptanceCost.subtract(fromSubs);
            // updateBalances(context, fromSubs, fromPaygo);

            // Just hold the acceptance cost in wallet, do not deduct it from credit or subscription balance.
            applyAcceptanceCost(context.getWallet(), subsAcceptanceCost);
            holdCredit = createHoldCredit(context.getWallet(), subsAcceptanceCost, CreditSource.HYBRID);
        }

        applyClaimCost(context.getWallet(), subsClaimCost);

        serviceProviderWalletService.save(context.getWallet());

        // Update the claim cost from subscription and paygo
        BigDecimal fromSubsClaim = subsBalance.min(subsClaimCost);
        BigDecimal fromPaygoClaim = subsClaimCost.subtract(fromSubsClaim);
        updateBalances(context, fromSubsClaim, fromPaygoClaim);

        // Set the currently used credit source
        context.setCreditSource(getCreditSource());

        // Set the currently used credit amount at this stage which is the claim cost
        // The acceptance cost will be deducted after the lead claim is accepted
        context.setUsedCreditAmount(claimCost);

        return holdCredit;
    }

    private void updateBalances(CreditContext context, BigDecimal fromSubs, BigDecimal fromPaygo) {
        if (fromSubs.compareTo(BigDecimal.ZERO) > 0) {
            ServiceProviderSubscription sub = context.getWallet().getSubscription();
            sub.setBalance(sub.getBalance().subtract(fromSubs));
            subscriptionService.save(sub);

            // Add to the claim cost
            claimCost = claimCost.add(fromSubs);
        }
        if (fromPaygo.compareTo(BigDecimal.ZERO) > 0) {
            PayGoCredit payGo = context.getWallet().getPaygoCredit();
            payGo.setBalance(payGo.getBalance().subtract(fromPaygo));
            payGoCreditService.save(payGo);

            // Add to the claim cost
            claimCost = claimCost.add(fromPaygo);
        }
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
        return CreditSource.HYBRID;
    }
}
