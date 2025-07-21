package com.muyategna.backend.billing.dto.service_provider_subscription;

import com.muyategna.backend.billing.enums.RenewalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderSubscriptionDto {
    private Long id;
    private Long providerId;
    private Long subscriptionPlanId;
    private Long upgradedFromId;
    private Long downgradedFromId;
    private RenewalStatusEnum renewalStatus;
    private Map<String, Object> extraData;
    private Boolean isActive;
    private BigDecimal initialCredits;
    private BigDecimal usedCredits;
    private Boolean autoRenew;
    private LocalDateTime lastBillingDate;
    private LocalDateTime nextBillingDate;
    private LocalDateTime cancelledAt;
    private LocalDateTime renewalAttemptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime endDate;
    private LocalDateTime trialPeriodStartedAt;
    private LocalDateTime trialPeriodEndsAt;
    private LocalDateTime gracePeriodStartedAt;
    private LocalDateTime gracePeriodEndsAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
