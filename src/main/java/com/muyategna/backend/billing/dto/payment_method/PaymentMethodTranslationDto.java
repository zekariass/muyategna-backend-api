package com.muyategna.backend.billing.dto.payment_method;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodTranslationDto {
    private Long id;
    private Long paymentMethodId;
    private Long languageId;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

