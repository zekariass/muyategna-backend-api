package com.muyategna.backend.billing.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public class SequenceNumberGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getNextInvoiceNumberSequence() {
        return ((Number) entityManager
                .createNativeQuery("SELECT nextval('invoice_number_seq')")
                .getSingleResult()).longValue();
    }

    public String generateInvoiceNumber(String countryCode) {
        Long sequenceValue = getNextInvoiceNumberSequence();
        String datePart = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("INV-%s-%s-%05d",
                countryCode.toUpperCase(),
                datePart,
                sequenceValue);
    }


    public Long getNextTransactionReferenceSequence() {
        return ((Number) entityManager
                .createNativeQuery("SELECT nextval('transaction_reference_seq')")
                .getSingleResult()).longValue();
    }

    public String generateTransactionNumber(String countryCode) {
        Long sequenceValue = getNextTransactionReferenceSequence();
        String datePart = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("TXN-%s-%s-%05d",
                countryCode.toUpperCase(),
                datePart,
                sequenceValue);
    }


    public Long getNextBankTransferTransactionReferenceSequence() {
        return ((Number) entityManager
                .createNativeQuery("SELECT nextval('bank_transfer_transaction_reference_seq')")
                .getSingleResult()).longValue();
    }

    public String generateBankTransferTransactionNumber(String countryCode) {
        Long sequenceValue = getNextBankTransferTransactionReferenceSequence();
        String datePart = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("BTX-%s-%s-%05d",
                countryCode.toUpperCase(),
                datePart,
                sequenceValue);
    }
}

