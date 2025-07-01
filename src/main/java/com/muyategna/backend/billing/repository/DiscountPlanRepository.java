package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.repository.custom.DiscountPlanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountPlanRepository extends JpaRepository<DiscountPlan, Long>, DiscountPlanRepositoryCustom {

}
