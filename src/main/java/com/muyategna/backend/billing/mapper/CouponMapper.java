package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.coupon.CouponDto;
import com.muyategna.backend.billing.entity.Coupon;

public final class CouponMapper {
    public static CouponDto toDto(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .usageLimit(coupon.getUsageLimit())
                .perUserLimit(coupon.getPerUserLimit())
                .totalUseCount(coupon.getTotalUseCount())
                .startsAt(coupon.getStartsAt())
                .expiresAt(coupon.getExpiresAt())
                .status(coupon.getStatus())
                .discountPlanId(coupon.getDiscountPlan().getId())
                .isGlobal(coupon.isGlobal())
                .build();
    }
}
