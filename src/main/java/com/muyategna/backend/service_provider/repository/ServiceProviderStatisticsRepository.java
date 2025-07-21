package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceProviderStatisticsRepository extends JpaRepository<ServiceProviderStatistics, Long> {
    List<ServiceProviderStatistics> findByProviderIdIn(List<Long> serviceProviderIds);

    ServiceProviderStatistics findByProviderId(Long serviceProviderId);
}
