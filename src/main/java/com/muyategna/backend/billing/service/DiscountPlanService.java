package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;

import java.math.BigDecimal;
import java.util.Optional;

public interface DiscountPlanService {
    DiscountPlanLocalizedDto getDiscountPlanById(Long discountPlanId);

    DiscountPlan getDiscountPlan(Long discountPlanId);

    Optional<DiscountPlan> getAndValidateDiscountPlan(Long discountPlanId, PaymentRequirement paymentRequirement);

    BigDecimal computeAmountWithDiscountPlan(BigDecimal amount, DiscountPlan discountPlan);
}
