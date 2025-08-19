package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.PayGoCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayGoCreditRepository extends JpaRepository<PayGoCredit, Long> {
    Optional<PayGoCredit> findByProviderId(Long id);
}
