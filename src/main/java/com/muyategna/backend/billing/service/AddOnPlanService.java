package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanDto;
import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import com.muyategna.backend.billing.entity.AddOnPlan;

import java.util.List;
import java.util.Optional;

public interface AddOnPlanService {
    AddOnPlanLocalizedDto getAddOnPlanById(Long planId);

    List<AddOnPlanLocalizedDto> getAddOnPlansByCountryId();

    AddOnPlanDto getAddOnPlan(Long addOnPlanId);

    Optional<AddOnPlan> getById(Long productPlanId);
}
