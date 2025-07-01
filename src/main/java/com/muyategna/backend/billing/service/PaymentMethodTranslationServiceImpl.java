package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.PaymentMethodTranslation;
import com.muyategna.backend.billing.repository.PaymentMethodTranslationRepository;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentMethodTranslationServiceImpl implements PaymentMethodTranslationService {

    private final PaymentMethodTranslationRepository repository;
    private final LanguageService languageService;

    @Autowired
    public PaymentMethodTranslationServiceImpl(PaymentMethodTranslationRepository repository, LanguageService languageService) {
        this.repository = repository;
        this.languageService = languageService;
    }

    @Override
    public Optional<PaymentMethodTranslation> getPaymentMethodTranslationByPaymentMethodId(Long paymentMethodId) {
        LanguageDto language = LanguageContextHolder.getLanguage();
        Optional<PaymentMethodTranslation> paymentMethodTranslation = repository.findByPaymentMethod_IdAndLanguage_Id(paymentMethodId, language.getId());
        if (paymentMethodTranslation.isPresent()) {
            return paymentMethodTranslation;
        }

        // Get for global language
        LanguageDto globalLanguage = languageService.getGlobalLanguage();
        Optional<PaymentMethodTranslation> globalTranslation = repository.findByPaymentMethod_IdAndLanguage_Id(paymentMethodId, globalLanguage.getId());
        if (globalTranslation.isPresent()) {
            return globalTranslation;
        }

        throw new ResourceNotFoundException("Payment method translation not found");
    }

    @Override
    public List<PaymentMethodTranslation> getPaymentMethodTranslationsByPaymentMethodIdIn(List<Long> paymentMethodIds) {
        LanguageDto language = LanguageContextHolder.getLanguage();
        List<PaymentMethodTranslation> paymentMethodTranslations = repository.findByPaymentMethod_IdInAndLanguage_Id(paymentMethodIds, language.getId());

        Map<Long, PaymentMethodTranslation> translationMap = paymentMethodTranslations.stream()
                .collect(Collectors.toMap(tr -> tr.getPaymentMethod().getId(), Function.identity()));

        List<Long> missingIds = paymentMethodIds
                .stream()
                .filter(id -> !translationMap.containsKey(id))
                .toList();

        LanguageDto globalLanguage = languageService.getGlobalLanguage();
        List<PaymentMethodTranslation> fallbackTranslations = repository.findByPaymentMethod_IdInAndLanguage_Id(missingIds, globalLanguage.getId());

        paymentMethodTranslations = new ArrayList<>(paymentMethodTranslations);
        paymentMethodTranslations.addAll(fallbackTranslations);

        return paymentMethodTranslations;
    }
}
