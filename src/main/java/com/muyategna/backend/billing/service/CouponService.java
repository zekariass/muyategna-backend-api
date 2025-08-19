package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.coupon.CouponDto;
import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponService {
    CouponDto getCouponByCode(String couponCode);

    Optional<Coupon> getAndValidateCoupon(String couponCode, PaymentRequirement paymentRequirement);

    CouponDto getCouponById(Long couponId);

    CouponDto getGlobalCouponForDiscountPlan(Long discountPlanId);

    BigDecimal computeAmountWithCoupon(BigDecimal amount, Coupon coupon);
}
