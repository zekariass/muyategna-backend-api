package com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOnPlanDiscountEligibilityCreateDto {
    private Long addOnPlanId;
    private Long discountPlanId;
    private Boolean isActive;
}
