package com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanDiscountEligibilityCreateDto {
    private Long subscriptionPlanId;
    private Long discountPlanId;
    private Boolean isActive;
}

