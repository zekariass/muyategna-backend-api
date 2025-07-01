package com.muyategna.backend.billing.dto.subscription_plan_translation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanTranslationCreateDto {
    private Long subscriptionPlanId;
    private Long languageId;
    private String displayName;
    private String description;
}

