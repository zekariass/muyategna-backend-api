package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.PaymentMethodTranslation;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodTranslationService {
    Optional<PaymentMethodTranslation> getPaymentMethodTranslationByPaymentMethodId(Long paymentMethodId);

    List<PaymentMethodTranslation> getPaymentMethodTranslationsByPaymentMethodIdIn(List<Long> paymentMethodIds);
}
