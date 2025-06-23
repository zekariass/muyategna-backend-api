package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderServiceRepository extends JpaRepository<ServiceProviderService, Long> {
    List<ServiceProviderService> findByProviderId(Long serviceProviderId);

    Optional<ServiceProviderService> findByIdAndProviderId(Long serviceProviderServiceId, Long serviceProviderId);
}
