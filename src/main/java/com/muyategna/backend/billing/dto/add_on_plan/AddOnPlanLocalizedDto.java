package com.muyategna.backend.billing.dto.add_on_plan;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOnPlanLocalizedDto {
    private Long id;
    //    private Long serviceId;
    private Long countryId;
    private String name;
    private String description;
    private BigDecimal priceAmount;
    private BigDecimal creditsIncluded;
    private Integer sortOrder;
    private Map<String, String> extraData;

    private List<DiscountPlanDto> discounts;

}

