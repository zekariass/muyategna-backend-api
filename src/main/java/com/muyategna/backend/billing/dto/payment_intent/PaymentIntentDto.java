package com.muyategna.backend.billing.dto.payment_intent;

import com.muyategna.backend.billing.enums.PayerEntityTypeEnum;
import com.muyategna.backend.billing.enums.PaymentIntentStatusEnum;
import com.muyategna.backend.billing.enums.ProductTypeEnum;
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
public class PaymentIntentDto {
    private Long id;
    private PayerEntityTypeEnum payerEntityType;
    private Long payerEntityId;
    private BigDecimal amountBeforeTax;
    private String currency;
    private ProductTypeEnum productType;
    private PaymentIntentStatusEnum status;
    private LocalDateTime paymentDueAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
