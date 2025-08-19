package com.muyategna.backend.billing.gateways.strategies;

import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;

import java.util.Map;

public interface PaymentStrategy {
    Map<String, Object> initializePayment(PaymentRequirement paymentRequirement);

    void processWebhook(Map<String, Object> payload);

    boolean verifyTransaction(Map<String, Object> params);
}
