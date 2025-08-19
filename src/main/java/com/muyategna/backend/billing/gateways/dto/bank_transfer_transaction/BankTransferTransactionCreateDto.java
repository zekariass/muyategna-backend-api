package com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferTransactionCreateDto {

    @NotBlank(message = "Transaction reference is required")
    private String txnRef;

    private String invoiceNumber;

    @NotBlank(message = "Payer full name is required")
    private String payerFullName;

    @NotBlank(message = "Payer bank name is required")
    private String payerBankName;

    private String payerBankBranchName;

    private String payerBankAccountNumber;

    @NotNull(message = "Transaction amount is required")
    private BigDecimal amountPaid;

    @NotBlank(message = "Transaction currency is required")
    private String currency;

    private String bankReferenceNumber;

    @NotNull(message = "Proof of payment is required")
    private MultipartFile proofOfPaymentFile;
}
