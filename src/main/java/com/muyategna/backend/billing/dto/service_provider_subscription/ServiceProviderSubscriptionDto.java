package com.muyategna.backend.billing.dto.service_provider_subscription;

import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
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
public class ServiceProviderSubscriptionDto {
    private Long id;
    private Long providerId;
    private SubscriptionPlanLocalizedDto subscriptionPlan;
    private BigDecimal initialCredits;
    private BigDecimal usedCredits;
    //    private LocalDateTime validUntil;
    private Long upgradedFromId;
    private Boolean isActive;
    private LocalDateTime expiresAt;
    private LocalDateTime subscribedAt;
}
