package com.muyategna.backend.billing.strategies.credit_usage_strategy;

import com.muyategna.backend.billing.entity.HoldCredit;
import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.billing.service.PayGoCreditService;
import com.muyategna.backend.billing.service.ServiceProviderWalletService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PayGoCreditStrategy implements CreditUsageStrategy {

    private final ServiceProviderWalletService serviceProviderWalletService;
    private final PayGoCreditService payGoCreditService;

    public PayGoCreditStrategy(ServiceProviderWalletService serviceProviderWalletService, PayGoCreditService payGoCreditService) {
        this.serviceProviderWalletService = serviceProviderWalletService;
        this.payGoCreditService = payGoCreditService;
    }

    @Override
    public HoldCredit useCredit(CreditContext context) {
        BigDecimal claimCost = context.getCreditCost().getPaygClaimCostPerLead();
        BigDecimal acceptanceCost = context.getCreditCost().getPaygAcceptanceCostPerLead();

        HoldCredit holdCredit = null;
        if (acceptanceCost.compareTo(BigDecimal.ZERO) > 0) {

            // Just hold the acceptance cost in wallet, do not deduct it from credit or subscription balance.
            applyAcceptanceCost(context.getWallet(), acceptanceCost);

            // Create a new hold credit
            holdCredit = createHoldCredit(context.getWallet(), acceptanceCost, CreditSource.PAYGO);
        }

        applyClaimCost(context.getWallet(), claimCost);

        // Save the wallet
        serviceProviderWalletService.save(context.getWallet());

        // Update the paygo credit
        updatePayGoCreditBalance(context.getWallet().getPaygoCredit(), claimCost);

        // Set the currently used credit source
        context.setCreditSource(getCreditSource());

        // Set the currently used credit amount at this stage which is the claim cost
        // The acceptance cost will be deducted after the lead claim is accepted
        context.setUsedCreditAmount(claimCost);

        return holdCredit;
    }

    private void updatePayGoCreditBalance(PayGoCredit payGoCredit, BigDecimal claimCost) {
        payGoCredit.setBalance(payGoCredit.getBalance().subtract(claimCost));
        payGoCreditService.save(payGoCredit);
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
        return CreditSource.PAYGO;
    }
}
