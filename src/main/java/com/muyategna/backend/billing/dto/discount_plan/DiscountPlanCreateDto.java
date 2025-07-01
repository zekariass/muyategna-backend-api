package com.muyategna.backend.billing.dto.discount_plan;

import com.muyategna.backend.billing.enums.DiscountUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountPlanCreateDto {
    private Long countryId;
    private String name;
    private String description;
    private BigDecimal fixedValue;
    private BigDecimal percentageValue;
    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;
    private Integer usageLimit;
    private Integer perUserLimit;
    private BigDecimal maxDiscountValue;
    private Boolean isActive;
    private Boolean couponRequired;
    private DiscountUserType appliesTo;
}
