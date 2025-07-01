package com.muyategna.backend.billing.dto.add_on_plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOnPlanLocalizedMinimalDto {
    private Long id;
    //    private Long serviceId;
    private Long countryId;
    private String name;
    private String description;
    private BigDecimal priceAmount;
    private Integer creditsIncluded;
    private Integer sortOrder;
}

