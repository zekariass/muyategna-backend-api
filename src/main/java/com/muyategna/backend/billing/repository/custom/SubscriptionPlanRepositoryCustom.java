package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepositoryCustom {
    Optional<SubscriptionPlan> findSubscriptionPlanByIdAndCountryId(Long subscriptionPlanId, Long countryId);

    List<SubscriptionPlan> findSubscriptionPlansByCountryId(Long countryId);
}
