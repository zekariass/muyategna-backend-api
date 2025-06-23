package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderTaxInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProviderTaxInfoRepository extends JpaRepository<ServiceProviderTaxInfo, Long> {
    Optional<ServiceProviderTaxInfo> findByIdAndServiceProviderId(Long serviceProviderTaxInfoId, Long serviceProviderId);
}
