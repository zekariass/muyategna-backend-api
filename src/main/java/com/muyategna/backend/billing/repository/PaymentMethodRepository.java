package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.repository.custom.PaymentMethodRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, PaymentMethodRepositoryCustom {
    Optional<PaymentMethod> findByIdAndCountryId(Long paymentMethodId, Long id);
}
