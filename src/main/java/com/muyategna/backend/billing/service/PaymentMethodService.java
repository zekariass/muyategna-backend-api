package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.payment_method.PaymentMethodLocalizedDto;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodLocalizedDto> getAllPaymentMethodsForCurrentCountry();

    PaymentMethodLocalizedDto getPaymentMethodById(Long paymentMethodId);
}
