package com.muyategna.backend.billing.gateways.strategies;

import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.repository.PaymentMethodRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyResolver {

    private final ChapaPaymentStrategy chapaPaymentStrategy;
    private final PesapalPaymentStrategy pesapalPaymentStrategy;
    private final BankTransferPaymentStrategy bankTransferPaymentStrategy;

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentStrategyResolver(ChapaPaymentStrategy chapaPaymentStrategy, PesapalPaymentStrategy pesapalPaymentStrategy, BankTransferPaymentStrategy bankTransferPaymentStrategy, PaymentMethodRepository paymentMethodRepository) {
        this.chapaPaymentStrategy = chapaPaymentStrategy;
        this.pesapalPaymentStrategy = pesapalPaymentStrategy;
        this.bankTransferPaymentStrategy = bankTransferPaymentStrategy;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public PaymentStrategy resolve(PaymentRequirement requirement) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(requirement.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found with ID: " + requirement.getPaymentMethodId()));

        if (paymentMethod.getCode().toLowerCase().contains("chapa")) {
            return chapaPaymentStrategy;
        } else if (paymentMethod.getCode().toLowerCase().contains("pesapal")) {
            return pesapalPaymentStrategy;
        } else if (paymentMethod.getCode().toLowerCase().contains("bank_transfer")) {
            return bankTransferPaymentStrategy;
        } else {
            throw new IllegalArgumentException("Unsupported payment method: " + requirement.getPaymentMethodId());
        }
    }
}
