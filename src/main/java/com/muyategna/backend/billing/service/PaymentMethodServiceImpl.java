package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.payment_method.PaymentMethodLocalizedDto;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.PaymentMethodTranslation;
import com.muyategna.backend.billing.mapper.PaymentMethodMapper;
import com.muyategna.backend.billing.repository.PaymentMethodRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repository;
    private final PaymentMethodTranslationService paymentMethodTranslationService;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository repository, PaymentMethodTranslationService paymentMethodTranslationService) {
        this.repository = repository;
        this.paymentMethodTranslationService = paymentMethodTranslationService;
    }

    @Override
    public PaymentMethodLocalizedDto getPaymentMethodById(Long paymentMethodId) {
        log.info("Retrieving payment method with ID: {}", paymentMethodId);
        PaymentMethod paymentMethod = repository.findById(paymentMethodId).orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
        PaymentMethodTranslation translation = paymentMethodTranslationService.getPaymentMethodTranslationByPaymentMethodId(paymentMethod.getId()).orElseThrow(() -> new ResourceNotFoundException("Payment method translation not found"));
        log.info("Payment method with ID: {} retrieved successfully", paymentMethodId);
        return PaymentMethodMapper.toLocalizedDto(paymentMethod, translation);
    }


    @Override
    public List<PaymentMethodLocalizedDto> getAllPaymentMethodsForCurrentCountry() {
        log.info("Retrieving all payment methods for current country");
        CountryDto countryDto = CountryContextHolder.getCountry();
        List<PaymentMethod> paymentMethods = repository.findPaymentMethodsByCountryId(countryDto.getId());

        List<Long> paymentMethodIds = paymentMethods.stream().map(PaymentMethod::getId).toList();
        List<PaymentMethodTranslation> paymentMethodTranslations = paymentMethodTranslationService.getPaymentMethodTranslationsByPaymentMethodIdIn(paymentMethodIds);

        log.info("Retrieved all payment methods for current country");
        return PaymentMethodMapper.toLocalizedDtoList(paymentMethods, paymentMethodTranslations);

    }

}
