package com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanDiscountEligibilityDto {
    private Long id;
    private SubscriptionPlanLocalizedDto subscriptionPlan;
    private DiscountPlanLocalizedDto discountPlan;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

