package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.CreditUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditUsageRepository extends JpaRepository<CreditUsage, Long> {
}
