package com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOnPlanDiscountEligibilityDto {
    private Long id;
    private AddOnPlanLocalizedDto addOnPlan;
    private DiscountPlanLocalizedDto discountPlan;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

