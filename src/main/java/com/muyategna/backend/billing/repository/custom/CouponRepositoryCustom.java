package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.Coupon;

import java.util.Optional;

public interface CouponRepositoryCustom {
    Optional<Coupon> findCouponById(Long couponId);

    Optional<Coupon> findCouponByCode(String couponCode);

    Optional<Coupon> findGlobalCouponForDiscountPlan(Long discountPlanId);
}
