package com.muyategna.backend.billing.dto.subscription_plan;

import com.muyategna.backend.billing.enums.BillingCycleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanLocalizedDto {
    private Long id;
    //    private Long serviceCategoryId;
    private Long countryId;
    private String displayName;
    private String description;
    private BigDecimal priceAmount;
    private BillingCycleEnum billingCycle;
    private Integer billingCycleCount;
    private Integer trialPeriodDays;
    private Integer creditsIncluded;
    private Integer sortOrder;
    private Boolean isDefault;
    private Boolean isActive;
    private String slug;
}

