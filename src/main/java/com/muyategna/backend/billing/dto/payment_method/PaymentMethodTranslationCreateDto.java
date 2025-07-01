package com.muyategna.backend.billing.dto.payment_method;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodTranslationCreateDto {
    private Long paymentMethodId;
    private Long languageId;
    private String displayName;
    private String description;
}

