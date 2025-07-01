package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.coupon.CouponDto;

public interface CouponService {
    CouponDto getCouponByCode(String couponCode);

    CouponDto getCouponById(Long couponId);

    CouponDto getGlobalCouponForDiscountPlan(Long discountPlanId);
}
