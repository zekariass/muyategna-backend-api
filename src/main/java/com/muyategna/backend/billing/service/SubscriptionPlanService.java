package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;

import java.util.List;

public interface SubscriptionPlanService {
    List<SubscriptionPlanLocalizedDto> getAllSubscriptionPlansForCurrentCountry();

    SubscriptionPlanLocalizedDto getSubscriptionPlanById(Long subscriptionPlanId);
}
