package com.muyategna.backend.billing.specification;

import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.location.entity.Country;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class DiscountPlanSpecification {

    public static Specification<DiscountPlan> discountPlanByCountry(Country country) {
        return (root, query, cb) -> country == null ? null : cb.equal(root.get("country"), country);
    }

    public static Specification<DiscountPlan> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<DiscountPlan> startsAfter(LocalDateTime startsAt) {
        return (root, query, cb) -> startsAt == null ? null : cb.greaterThanOrEqualTo(root.get("startsAt"), startsAt);
    }

    public static Specification<DiscountPlan> endsBefore(LocalDateTime expiresAt) {
        return (root, query, cb) -> expiresAt == null ? null : cb.lessThanOrEqualTo(root.get("expiresAt"), expiresAt);
    }

    public static Specification<DiscountPlan> checkUsageLimit() {
        return (root, query, cb) -> cb.lessThan(root.get("totalUseCount"), root.get("usageLimit"));
    }

    public static Specification<DiscountPlan> checkPerUserLimit(Integer perUserLimit) {
        return (root, query, cb) -> cb.lessThan(root.get("totalUseCount"), perUserLimit);
    }
}
