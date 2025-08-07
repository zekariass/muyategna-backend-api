package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderStatisticsRepository extends JpaRepository<ServiceProviderStatistics, Long> {

    @Query("""
            SELECT s FROM ServiceProviderStatistics s
            JOIN FETCH s.provider
            WHERE s.provider.id IN :serviceProviderIds
            """)
    List<ServiceProviderStatistics> findAllByServiceProviderIds(List<Long> serviceProviderIds);

    @Query("""
            SELECT s FROM ServiceProviderStatistics s
            JOIN FETCH s.provider
            WHERE s.provider.id = :serviceProviderId
            """)
    ServiceProviderStatistics findByProviderIdWithProvider(Long serviceProviderId);

    Optional<ServiceProviderStatistics> findByProviderId(Long id);
}
