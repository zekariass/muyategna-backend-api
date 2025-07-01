package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.SubscriptionPlanTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanTranslationRepository extends JpaRepository<SubscriptionPlanTranslation, Long> {
    Optional<SubscriptionPlanTranslation> findBySubscriptionPlanIdAndLanguageId(Long subscriptionPlanId, long languageId);

    List<SubscriptionPlanTranslation> findBySubscriptionPlan_IdInAndLanguage_Id(List<Long> subscriptionPlanIds, long id);
}
