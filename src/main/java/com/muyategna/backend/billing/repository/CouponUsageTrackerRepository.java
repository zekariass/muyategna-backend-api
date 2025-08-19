package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.CouponUsageTracker;
import com.muyategna.backend.billing.enums.UserEntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponUsageTrackerRepository extends JpaRepository<CouponUsageTracker, Long> {
    Optional<CouponUsageTracker> findByCouponIdAndCouponUserTypeAndCouponUserId(Long couponId, UserEntityType couponUserType, Long couponUserId);
}
