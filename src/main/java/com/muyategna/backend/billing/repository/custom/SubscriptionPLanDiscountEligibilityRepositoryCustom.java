package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.SubscriptionPlanDiscountEligibility;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPLanDiscountEligibilityRepositoryCustom {
    Optional<SubscriptionPlanDiscountEligibility> findSubscriptionPlanDiscountEligibilityByIdAndCountryId(Long eligibilityId, Long countryId);

    List<SubscriptionPlanDiscountEligibility> findAllBySubscriptionPlanIdAndByCountryId(Long subscriptionPlanId, Long countryId);
}
