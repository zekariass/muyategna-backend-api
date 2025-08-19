package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.AddOnPlanDiscountEligibility;
import com.muyategna.backend.billing.repository.custom.AddOnPlanDiscountEligibilityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddOnPlanDiscountEligibilityRepository
        extends JpaRepository<AddOnPlanDiscountEligibility, Long>,
        AddOnPlanDiscountEligibilityRepositoryCustom {


    @Query(
            """
                    SELECT DISTINCT a FROM AddOnPlanDiscountEligibility a
                    JOIN FETCH a.discountPlan
                    JOIN FETCH a.addOnPlan
                    WHERE a.addOnPlan.id IN :addOnPlanIds AND a.isActive = :isActive
                    """
    )
    List<AddOnPlanDiscountEligibility> findAllActiveLocalizedAddOnPlanDiscountEligibilityByAddOnPlanId(
            @Param("addOnPlanIds") List<Long> addOnPlanIds,
            @Param("isActive") boolean isActive);
}
