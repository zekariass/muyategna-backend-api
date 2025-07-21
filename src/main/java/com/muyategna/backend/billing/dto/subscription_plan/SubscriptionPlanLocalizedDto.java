package com.muyategna.backend.billing.dto.subscription_plan;

import com.muyategna.backend.billing.enums.BillingCycleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

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
//    private Integer leadsIncluded = 0;
    private BigDecimal creditsIncluded;
    private Boolean upgradeable = true;
    private Boolean downgradeable = true;
    private Map<String, Object> extraData;
    private Integer sortOrder;
    private Boolean isDefault;
    private Boolean isActive;
}

