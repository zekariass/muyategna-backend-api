package com.muyategna.backend.billing.dto.discount_plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountPlanTranslationUpdateDto {
    private String displayName;
    private String description;
}

