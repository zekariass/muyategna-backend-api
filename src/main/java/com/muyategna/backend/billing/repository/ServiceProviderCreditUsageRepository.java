package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.ServiceProviderCreditUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderCreditUsageRepository extends JpaRepository<ServiceProviderCreditUsage, Long> {
}
