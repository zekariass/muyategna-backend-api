package com.muyategna.backend.billing.gateways.service;

import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionCreateDto;

import java.util.Map;

public interface PaymentService {
    Map<String, Object> initializePayment(PaymentRequirement paymentRequirement);

    void processWebhook(Map<String, Object> payload);


    void reportBankTransferPayment(BankTransferTransactionCreateDto bankTransferDto);

    void verifyBankTransferTransaction(String txnRef);

    void cancelBankTransferTransaction(String txnRef);
}
