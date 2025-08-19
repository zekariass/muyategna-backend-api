package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility.AddOnPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.entity.AddOnPlanDiscountEligibility;

import java.util.List;

public interface AddOnPlanDiscountEligibilityService {
    AddOnPlanDiscountEligibilityDto getAddOnPlanDiscountEligibilityById(Long eligibilityId);

    List<AddOnPlanDiscountEligibilityDto> getAllAddOnPlanDiscountEligibilityByAddOnPlanId(Long addOnPlanId);

    List<AddOnPlanDiscountEligibility> getAllActiveAddOnPlanDiscountEligibilityByAddOnPlanId(List<Long> addOnPlanIds, boolean isActive);
}
