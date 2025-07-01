package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.AddOnPlan;
import com.muyategna.backend.billing.repository.custom.AddOnPlanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnPlanRepository extends JpaRepository<AddOnPlan, Long>, AddOnPlanRepositoryCustom {
}
