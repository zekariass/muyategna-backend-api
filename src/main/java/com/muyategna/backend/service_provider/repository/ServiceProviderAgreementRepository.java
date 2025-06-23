package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderAgreementRepository extends JpaRepository<ServiceProviderAgreement, Long> {
    List<ServiceProviderAgreement> findByProviderId(Long serviceProviderId);

    Optional<ServiceProviderAgreement> findByIdAndProviderId(Long agreementId, Long serviceProviderId);
}
