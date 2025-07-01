package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.AddOnPlanDiscountEligibility;

import java.util.List;
import java.util.Optional;

public interface AddOnPlanDiscountEligibilityRepositoryCustom {
    Optional<AddOnPlanDiscountEligibility> findAddOnPlanDiscountEligibilityByIdAndCountryId(Long eligibilityId, Long countryId);

    List<AddOnPlanDiscountEligibility> findAllAddOnPlanDiscountEligibilityByAddOnPlanIdAndCountryId(Long addOnPlanId, Long countryId);
}
