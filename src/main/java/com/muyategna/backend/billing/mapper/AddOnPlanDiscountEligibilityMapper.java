package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility.AddOnPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.entity.AddOnPlanDiscountEligibility;
import com.muyategna.backend.billing.entity.AddOnPlanTranslation;
import com.muyategna.backend.billing.entity.DiscountPlanTranslation;

public final class AddOnPlanDiscountEligibilityMapper {

    public static AddOnPlanDiscountEligibilityDto toDto(AddOnPlanDiscountEligibility addOnPlanDiscountEligibility,
                                                        AddOnPlanTranslation addOnPlanTranslation,
                                                        DiscountPlanTranslation discountPlanTranslation) {
        return AddOnPlanDiscountEligibilityDto.builder()
                .id(addOnPlanDiscountEligibility.getId())
                .addOnPlan(AddOnPlanMapper.toLocalizedDto(addOnPlanDiscountEligibility.getAddOnPlan(), addOnPlanTranslation))
                .discountPlan(DiscountPlanMapper.toLocalizedDto(addOnPlanDiscountEligibility.getDiscountPlan(), discountPlanTranslation))
                .isActive(addOnPlanDiscountEligibility.getIsActive())
                .createdAt(addOnPlanDiscountEligibility.getCreatedAt())
                .updatedAt(addOnPlanDiscountEligibility.getUpdatedAt())
                .build();
    }
}
