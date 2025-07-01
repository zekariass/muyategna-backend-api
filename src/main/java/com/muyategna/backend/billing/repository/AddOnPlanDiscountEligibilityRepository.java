package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.AddOnPlanDiscountEligibility;
import com.muyategna.backend.billing.repository.custom.AddOnPlanDiscountEligibilityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnPlanDiscountEligibilityRepository
        extends JpaRepository<AddOnPlanDiscountEligibility, Long>,
        AddOnPlanDiscountEligibilityRepositoryCustom {
}
