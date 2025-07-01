package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.SubscriptionPlan;
import com.muyategna.backend.billing.repository.custom.SubscriptionPlanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long>, SubscriptionPlanRepositoryCustom {
}
