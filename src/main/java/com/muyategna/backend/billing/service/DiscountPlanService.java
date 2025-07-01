package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;

public interface DiscountPlanService {
    DiscountPlanLocalizedDto getDiscountPlanById(Long discountPlanId);
}
