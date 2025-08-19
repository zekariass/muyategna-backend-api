package com.muyategna.backend.billing.gateways.service.producer;

import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionProofQueueMessage;

public interface PaymentProducerService {
    void enqueueBankTransferPaymentProofUpload(BankTransferTransactionProofQueueMessage queueMessage);
}
