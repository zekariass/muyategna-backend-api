package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderRankingWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProviderRankingWeightRepository extends JpaRepository<ServiceProviderRankingWeight, Long> {
    Optional<ServiceProviderRankingWeight> findByIsActive(boolean b);
}
