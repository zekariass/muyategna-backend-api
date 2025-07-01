package com.muyategna.backend.billing.specification;

import com.muyategna.backend.billing.entity.AddOnPlan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AddOnPlanSpecification {

    public static Specification<AddOnPlan> ofId(Long id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<AddOnPlan> isActive(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<AddOnPlan> forCountry(Long countryId) {
        return (root, query, cb) -> countryId == null ? null : cb.equal(root.get("country").get("id"), countryId);
    }

    public static Specification<AddOnPlan> forService(Long serviceId) {
        return (root, query, cb) -> serviceId == null ? null : cb.equal(root.get("service").get("id"), serviceId);
    }

    public static Specification<AddOnPlan> isNotExpired() {
        return (root, query, cb) -> cb.greaterThan(root.get("expiresAt"), LocalDateTime.now());
    }
}
