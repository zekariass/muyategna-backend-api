package com.muyategna.backend.billing.dto.add_on_plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOnPlanTranslationCreateDto {
    private Long addOnPlanId;
    private Long languageId;
    private String displayName;
    private String description;
}
