package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.AccountingTypeEnum;
import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import com.muyategna.backend.billing.enums.TransactionTypeEnum;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;

import java.util.Optional;

public interface TransactionService {
    Transaction createTransaction(Invoice invoice, AccountingTypeEnum dr, PaymentRequirement paymentRequirement, TransactionStatusEnum transactionStatusEnum, PaymentMethod paymentMethod, TransactionTypeEnum original);

    Optional<Transaction> getTransactionByTxnRef(String txnRef);

    Transaction saveTransaction(Transaction txn);

    Optional<Transaction> getTransactionByTxnRefWithInvoice(String txnRef);
}
