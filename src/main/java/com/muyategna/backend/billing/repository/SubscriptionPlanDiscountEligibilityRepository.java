package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.SubscriptionPlanDiscountEligibility;
import com.muyategna.backend.billing.repository.custom.SubscriptionPLanDiscountEligibilityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanDiscountEligibilityRepository
        extends JpaRepository<SubscriptionPlanDiscountEligibility, Long>,
        SubscriptionPLanDiscountEligibilityRepositoryCustom {
}
