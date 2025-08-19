package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.BankTransferTransaction;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.user.entity.UserProfile;

import java.util.Optional;

public interface BankTransferTransactionService {
    BankTransferTransaction createPendingBankTransferTransaction(Transaction transaction, Invoice invoice, ServiceProvider serviceProvider, UserProfile userProfile);

    Optional<BankTransferTransaction> getBankTransferTransactionByTxnRefWithTransaction(String txnRef);

    void saveBankTransferTransaction(BankTransferTransaction bankTransferTransaction);
}
