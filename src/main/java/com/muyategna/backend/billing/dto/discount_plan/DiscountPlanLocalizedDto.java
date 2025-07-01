package com.muyategna.backend.billing.dto.discount_plan;

import com.muyategna.backend.billing.dto.coupon.CouponDto;
import com.muyategna.backend.billing.enums.DiscountUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountPlanLocalizedDto {
    private Long id;
    private Long countryId;
    private String name;
    private String description;
    private BigDecimal fixedValue;
    private BigDecimal percentageValue;
    private Boolean isActive;
    private CouponDto coupon;
    private Boolean couponRequired;
    private DiscountUserType appliesTo;

}

