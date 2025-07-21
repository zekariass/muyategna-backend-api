package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.ServiceProviderSubscriptionUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderSubscriptionUsageRepository extends JpaRepository<ServiceProviderSubscriptionUsage, Long> {
}
