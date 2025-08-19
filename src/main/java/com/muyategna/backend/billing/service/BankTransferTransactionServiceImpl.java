package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.BankTransferTransaction;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.BankTransferStatus;
import com.muyategna.backend.billing.repository.BankTransferTransactionRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BankTransferTransactionServiceImpl implements BankTransferTransactionService {

    private final BankTransferTransactionRepository bankTransferTransactionRepository;
    private final SequenceNumberGenerator sequenceNumberGenerator;


    @Autowired
    public BankTransferTransactionServiceImpl(BankTransferTransactionRepository bankTransferTransactionRepository, SequenceNumberGenerator sequenceNumberGenerator) {
        this.bankTransferTransactionRepository = bankTransferTransactionRepository;
        this.sequenceNumberGenerator = sequenceNumberGenerator;
    }

    @Override
    public BankTransferTransaction createPendingBankTransferTransaction(Transaction transaction, Invoice invoice, ServiceProvider serviceProvider, UserProfile userProfile) {

        String payerFullName = null;
        if (serviceProvider != null) {
            payerFullName = serviceProvider.getBusinessName();
        } else if (userProfile != null) {
            payerFullName = userProfile.getFirstName() + " " + userProfile.getLastName();
        } else {
            throw new IllegalArgumentException("Service provider or user profile must not be null");
        }

        log.info("Creating pending bank transfer transaction for transaction ID: {} and invoice ID: {}",
                transaction.getId(), invoice.getId());
        BankTransferTransaction bankTransferTransaction = new BankTransferTransaction();
        bankTransferTransaction.setTransaction(transaction);
        bankTransferTransaction.setStatus(BankTransferStatus.PENDING);
        bankTransferTransaction.setAmountDue(transaction.getAmountPaid());
        bankTransferTransaction.setPayerFullName(payerFullName);
        bankTransferTransaction.setTxnRef(sequenceNumberGenerator.generateBankTransferTransactionNumber(
                CountryContextHolder.getCountry().getCountryIsoCode2()
        ));
        bankTransferTransaction.setCurrency(transaction.getCurrency());

        BankTransferTransaction savedBankTransferTransaction = bankTransferTransactionRepository.save(bankTransferTransaction);

        log.info("Created pending bank transfer transaction for transaction ID: {} and invoice ID: {}",
                transaction.getId(), invoice.getId());

        return savedBankTransferTransaction;
    }

    @Override
    public Optional<BankTransferTransaction> getBankTransferTransactionByTxnRefWithTransaction(String txnRef) {
        log.info("Getting bank transfer transaction by txnRef: {}", txnRef);
        return bankTransferTransactionRepository.findByTxnRefWithTransaction(txnRef);
    }

    @Override
    public void saveBankTransferTransaction(BankTransferTransaction bankTransferTransaction) {
        log.info("Saving bank transfer transaction with ID: {}", bankTransferTransaction.getId());
        bankTransferTransactionRepository.save(bankTransferTransaction);
        log.info("Saved bank transfer transaction with ID: {}", bankTransferTransaction.getId());
    }
}
