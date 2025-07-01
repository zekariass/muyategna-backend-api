package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.DiscountPlanTranslation;

import java.util.Optional;

public interface DiscountPlanTranslationService {
    Optional<DiscountPlanTranslation> getDiscountPlanTranslationByDiscountPlanId(Long discountPlanId);

    DiscountPlanTranslation getSubscriptionPlanTranslationById(Long id);
}
