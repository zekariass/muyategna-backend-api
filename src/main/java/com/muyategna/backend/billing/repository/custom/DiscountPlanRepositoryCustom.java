package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.DiscountPlan;

import java.util.Optional;

public interface DiscountPlanRepositoryCustom {
    Optional<DiscountPlan> findActiveDiscountPlanByIdAndCountryId(Long discountPlanId, Long countryId);
}
