package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;

import java.util.List;

public interface AddOnPlanService {
    AddOnPlanLocalizedDto getAddOnPlanById(Long planId);

    List<AddOnPlanLocalizedDto> getAddOnPlansByCountryId();
}
