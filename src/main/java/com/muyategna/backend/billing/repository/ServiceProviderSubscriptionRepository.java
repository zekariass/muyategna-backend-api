package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.repository.custom.ServiceProviderSubscriptionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProviderSubscriptionRepository extends JpaRepository<ServiceProviderSubscription, Long>, ServiceProviderSubscriptionRepositoryCustom {
    Optional<ServiceProviderSubscription> findByProviderIdAndIsActiveTrue(Long serviceProviderId);
}
