package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility.SubscriptionPlanDiscountEligibilityDto;

import java.util.List;

public interface SubscriptionPlanDiscountEligibilityService {
    SubscriptionPlanDiscountEligibilityDto getSubscriptionPlanDiscountEligibilityById(Long eligibilityId);

    List<SubscriptionPlanDiscountEligibilityDto> getAllSubscriptionPlanEligibilityBySubscriptionPlanId(Long subscriptionPlanId);
}
