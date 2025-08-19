package com.muyategna.backend.billing.dto.payment_intent;

import com.muyategna.backend.billing.enums.PaymentIntentStatusEnum;
import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.enums.UserEntityType;
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
public class PaymentIntentCreateDto {
    private UserEntityType userEntityType;
    private Long payerEntityId;
    private BigDecimal amountBeforeTax;
    private String currency;
    private ProductType productType;
    private PaymentIntentStatusEnum status;
    private LocalDateTime paymentDueAt;
}

