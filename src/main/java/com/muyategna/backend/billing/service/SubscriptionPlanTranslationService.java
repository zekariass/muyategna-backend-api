package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.SubscriptionPlanTranslation;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanTranslationService {
    Optional<SubscriptionPlanTranslation> getSubscriptionPlanTranslationBySubscriptionPlanId(Long subscriptionPlanId);

    List<SubscriptionPlanTranslation> getSubscriptionPlanTranslationsBySubscriptionPlanIds(List<Long> subscriptionPlanIds);
}
