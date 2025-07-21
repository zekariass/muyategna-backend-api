package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.ServiceCreditCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceCreditCostRepository extends JpaRepository<ServiceCreditCost, Long> {
    Optional<ServiceCreditCost> findByServiceId(Long serviceId);
}
