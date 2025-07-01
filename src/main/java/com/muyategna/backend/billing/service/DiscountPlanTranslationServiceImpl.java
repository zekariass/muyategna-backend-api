package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.DiscountPlanTranslation;
import com.muyategna.backend.billing.repository.DiscountPlanTranslationRepository;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DiscountPlanTranslationServiceImpl implements DiscountPlanTranslationService {

    private final DiscountPlanTranslationRepository discountPlanTranslationRepository;
    private final LanguageService languageService;

    @Autowired
    public DiscountPlanTranslationServiceImpl(DiscountPlanTranslationRepository discountPlanTranslationRepository, LanguageService languageService) {
        this.discountPlanTranslationRepository = discountPlanTranslationRepository;
        this.languageService = languageService;
    }

    @Override
    public Optional<DiscountPlanTranslation> getDiscountPlanTranslationByDiscountPlanId(Long discountPlanId) {
        log.info("Getting discount plan translation by discount plan id: {}", discountPlanId);
        LanguageDto language = LanguageContextHolder.getLanguage();
        Optional<DiscountPlanTranslation> discountPlanTranslation = discountPlanTranslationRepository.findByDiscountPlanIdAndLanguageId(discountPlanId, language.getId());
        if (discountPlanTranslation.isPresent()) {
            return discountPlanTranslation;
        }

        LanguageDto globalLanguage = languageService.getGlobalLanguage();
        discountPlanTranslation = discountPlanTranslationRepository.findByDiscountPlanIdAndLanguageId(discountPlanId, globalLanguage.getId());
        return discountPlanTranslation;

    }

    @Override
    public DiscountPlanTranslation getSubscriptionPlanTranslationById(Long id) {
        log.info("Getting discount plan translation by id: {}", id);
        DiscountPlanTranslation discountPlanTranslation = discountPlanTranslationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Discount plan translation not found with id: " + id + ". Please check the database."));
        log.info("Discount plan translation found with id: {}", id);
        return discountPlanTranslation;
    }
}
