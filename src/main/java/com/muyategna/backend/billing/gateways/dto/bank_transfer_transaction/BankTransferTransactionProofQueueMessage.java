package com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferTransactionProofQueueMessage {
    private Long transactionId;
    private String txnRef;
    private byte[] fileContent;

    private String originalFilename;
    private String newFilename;
    private String extension;
    private String contentType;
}
