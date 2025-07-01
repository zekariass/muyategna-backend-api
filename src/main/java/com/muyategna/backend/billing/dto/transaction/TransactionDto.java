package com.muyategna.backend.billing.dto.transaction;

import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private Long paymentIntentId;
    private Long paymentMethodId;
    private BigDecimal subTotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal amountPaid;
    private String txnReference;
    private String currency;
    private String description;
    private TransactionStatusEnum status;
    private Long invoiceId;
    private LocalDateTime createdAt;
}

