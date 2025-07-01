package com.muyategna.backend.billing.dto.service_provider_add_on;

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
public class ServiceProviderAddOnCreateDto {
    private Long providerId;
    private Long addOnPlanId;
    private BigDecimal initialCredits;
    private BigDecimal usedCredits;
    private LocalDateTime purchasedAt;
    private Boolean isActive;
    private LocalDateTime expiresAt;
}

