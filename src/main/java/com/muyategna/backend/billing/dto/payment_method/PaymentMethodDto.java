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
public class PaymentMethodDto {
    private Long id;
    private Long countryId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

