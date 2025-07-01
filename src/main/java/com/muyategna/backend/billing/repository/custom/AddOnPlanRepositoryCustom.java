package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.AddOnPlan;

import java.util.List;
import java.util.Optional;

public interface AddOnPlanRepositoryCustom {
    Optional<AddOnPlan> findAddOnPlanByIdAndCountryId(Long addOnPlanId, Long countryId);

    List<AddOnPlan> findAllAddOnPlanByCountryId(Long countryId);
}
