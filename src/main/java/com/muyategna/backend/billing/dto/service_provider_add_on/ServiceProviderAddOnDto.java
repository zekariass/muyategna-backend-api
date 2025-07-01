package com.muyategna.backend.billing.dto.service_provider_add_on;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
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
public class ServiceProviderAddOnDto {
    private Long id;
    private Long providerId;
    private AddOnPlanLocalizedDto addOnPlan;
    private BigDecimal initialCredits;
    private BigDecimal usedCredits;
    private LocalDateTime purchasedAt;
    private Boolean isActive;
    private LocalDateTime expiresAt;
}

