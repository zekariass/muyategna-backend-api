package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.ServiceProviderCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ServiceProviderCreditRepository extends JpaRepository<ServiceProviderCredit, Long> {

    @Query("SELECT COUNT(s) > 0 FROM ServiceProviderCredit s " +
            "WHERE s.providerId = :serviceProviderId " +
            "AND s.balance >= :paygCreditsPerLead")
    Boolean hasServiceProviderEnoughCredit(@Param("serviceProviderId") Long serviceProviderId,
                                           @Param("paygCreditsPerLead") BigDecimal paygCreditsPerLead);

    Optional<ServiceProviderCredit> findByProviderId(Long serviceProviderId);
}
