package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.repository.custom.PaymentMethodRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, PaymentMethodRepositoryCustom {
}
