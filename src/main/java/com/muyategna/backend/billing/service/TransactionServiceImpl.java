package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.AccountingTypeEnum;
import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import com.muyategna.backend.billing.enums.TransactionTypeEnum;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SequenceNumberGenerator sequenceNumberGenerator;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, SequenceNumberGenerator sequenceNumberGenerator) {
        this.transactionRepository = transactionRepository;
        this.sequenceNumberGenerator = sequenceNumberGenerator;
    }

    @Override
    public Transaction createTransaction(
            Invoice invoice,
            AccountingTypeEnum dr,
            PaymentRequirement paymentRequirement,
            TransactionStatusEnum transactionStatusEnum,
            PaymentMethod paymentMethod,
            TransactionTypeEnum txnType) {

        Transaction transaction = new Transaction();
        transaction.setInvoice(invoice);
        transaction.setAccountingType(dr);
        transaction.setAmountPaid(invoice.getTotalAmount());
        transaction.setPaymentMethod(paymentMethod);
        transaction.setStatus(transactionStatusEnum);
        transaction.setCurrency(invoice.getCurrency());
        transaction.setOriginalTxn(null);
        transaction.setDescription("Payment for invoice: " + invoice.getInvoiceNumber());
        transaction.setExtraData(null);
//        transaction.setGatewayRef(null);
        transaction.setRefundedAmount(BigDecimal.ZERO);
        transaction.setTxnType(txnType);

        // Generate transaction number using sequence number generator
        String transactionNumber = sequenceNumberGenerator.generateTransactionNumber(invoice.getCountry().getCountryIsoCode2());
        transaction.setTxnRef(transactionNumber);

        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionByTxnRef(String txnRef) {
        log.info("Getting transaction by txnRef: {}", txnRef);
        return transactionRepository.findByTxnRef(txnRef);
    }

    @Override
    public Transaction saveTransaction(Transaction txn) {
        log.info("Saving transaction: {}", txn);
        return transactionRepository.save(txn);
    }

    @Override
    public Optional<Transaction> getTransactionByTxnRefWithInvoice(String txnRef) {
        log.info("Getting transaction by txnRef with invoice: {}", txnRef);
        return transactionRepository.findByTxnRefWithInvoice(txnRef);
    }
}
