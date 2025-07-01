package com.muyategna.backend.billing.dto.service_provider_subscription;

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
public class ServiceProviderSubscriptionCreateDto {
    private Long id;
    private Long providerId;
    private Long subscriptionPlanId;
    //    private Long paymentIntentId;
    private BigDecimal initialCredits;
    private BigDecimal usedCredits;
    //    private LocalDateTime validUntil;
    private Long upgradedFromId;
    private Boolean isActive;
    private LocalDateTime expiresAt;
    private LocalDateTime subscribedAt;
}
