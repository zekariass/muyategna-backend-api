package com.muyategna.backend.billing.gateways.service;

import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionCreateDto;

public interface BankTransferService {
    void reportBankTransferPayment(BankTransferTransactionCreateDto bankTransferDto);

    void verifyBankTransferPayment(String txnRef);

    void cancelBankTransferPayment(String txnRef);
}
