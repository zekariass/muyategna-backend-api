package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.DiscountUsageTracker;
import com.muyategna.backend.billing.enums.UserEntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountUsageTrackerRepository extends JpaRepository<DiscountUsageTracker, Long> {
    Optional<DiscountUsageTracker> findByDiscountPlanIdAndDiscountUserTypeAndDiscountUserId(Long discountPlanId, UserEntityType discountUserType, Long discountUserId);
}
