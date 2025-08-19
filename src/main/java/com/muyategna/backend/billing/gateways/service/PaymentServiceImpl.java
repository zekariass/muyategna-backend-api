package com.muyategna.backend.billing.gateways.service;

import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionCreateDto;
import com.muyategna.backend.billing.gateways.strategies.PaymentStrategy;
import com.muyategna.backend.billing.gateways.strategies.PaymentStrategyResolver;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentStrategyResolver paymentStrategyResolver;
    private PaymentStrategy paymentStrategy;
    private final BankTransferService bankTransferService;

    @Autowired
    public PaymentServiceImpl(PaymentStrategyResolver paymentStrategyResolver, BankTransferService bankTransferService) {
        this.paymentStrategyResolver = paymentStrategyResolver;
        this.bankTransferService = bankTransferService;
    }


    @Override
    public Map<String, Object> initializePayment(PaymentRequirement paymentRequirement) {

        ProductType productType = paymentRequirement.getProductType();
        Long addOnPlanId = paymentRequirement.getAddOnPlanId();
        Long subscriptionPlanId = paymentRequirement.getSubscriptionPlanId();

        validateRequestedProduct(productType, addOnPlanId, subscriptionPlanId);

        paymentStrategy = paymentStrategyResolver.resolve(paymentRequirement);
        if (paymentStrategy != null) {
            return paymentStrategy.initializePayment(paymentRequirement);
        }
        return Map.of();
    }

    private static void validateRequestedProduct(ProductType productType, Long addOnPlanId, Long subscriptionPlanId) {
        if (productType == ProductType.ADD_ON && addOnPlanId == null) {
            throw new BadRequestException("Add-on plan ID is required for add-on product type");
        }

        if (productType == ProductType.SUBSCRIPTION && subscriptionPlanId == null) {
            throw new BadRequestException("Subscription plan ID is required for subscription product type");
        }

        if (addOnPlanId != null && subscriptionPlanId != null) {
            throw new BadRequestException("Cannot specify both add-on plan ID and subscription plan ID");
        }
    }


    @Override
    public void processWebhook(Map<String, Object> payload) {
        paymentStrategy.processWebhook(payload);
    }

    @Override
    public void reportBankTransferPayment(BankTransferTransactionCreateDto bankTransferDto) {
        bankTransferService.reportBankTransferPayment(bankTransferDto);
    }

    @Override
    public void verifyBankTransferTransaction(String txnRef) {
        bankTransferService.verifyBankTransferPayment(txnRef);
    }

    @Override
    public void cancelBankTransferTransaction(String txnRef) {
        bankTransferService.cancelBankTransferPayment(txnRef);
    }
}
