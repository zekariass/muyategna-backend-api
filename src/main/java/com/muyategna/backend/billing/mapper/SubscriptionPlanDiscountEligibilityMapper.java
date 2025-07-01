package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility.SubscriptionPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.entity.DiscountPlanTranslation;
import com.muyategna.backend.billing.entity.SubscriptionPlanDiscountEligibility;
import com.muyategna.backend.billing.entity.SubscriptionPlanTranslation;

public final class SubscriptionPlanDiscountEligibilityMapper {

    public static SubscriptionPlanDiscountEligibilityDto toDto(SubscriptionPlanDiscountEligibility eligibility,
                                                               SubscriptionPlanTranslation subTranslation,
                                                               DiscountPlanTranslation disTranslation) {
        return SubscriptionPlanDiscountEligibilityDto.builder()
                .id(eligibility.getId())
                .subscriptionPlan(SubscriptionPlanMapper.toLocalizedDto(eligibility.getSubscriptionPlan(), subTranslation))
                .discountPlan(DiscountPlanMapper.toLocalizedDto(eligibility.getDiscountPlan(), disTranslation))
                .isActive(eligibility.getIsActive())
                .build();
    }
}
