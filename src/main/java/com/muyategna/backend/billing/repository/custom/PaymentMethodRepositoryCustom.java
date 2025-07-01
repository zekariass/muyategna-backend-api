package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepositoryCustom {
    Optional<PaymentMethod> findPaymentMethodByIdAndCountryId(Long paymentMethodId, Long countryId);

    List<PaymentMethod> findPaymentMethodsByCountryId(Long countryId);
}
