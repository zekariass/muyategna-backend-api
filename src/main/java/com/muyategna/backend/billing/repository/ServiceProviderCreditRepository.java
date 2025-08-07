package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.PayGoCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ServiceProviderCreditRepository extends JpaRepository<PayGoCredit, Long> {

    @Query("SELECT COUNT(s) > 0 FROM PayGoCredit s " +
            "WHERE s.provider.id = :serviceProviderId " +
            "AND s.balance >= :paygCreditsPerLead")
    Boolean hasServiceProviderEnoughCredit(@Param("serviceProviderId") Long serviceProviderId,
                                           @Param("paygCreditsPerLead") BigDecimal paygCreditsPerLead);

    Optional<PayGoCredit> findByProvider_id(Long serviceProviderId);
}
