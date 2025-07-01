package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.DiscountPlanTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountPlanTranslationRepository extends JpaRepository<DiscountPlanTranslation, Long> {
    Optional<DiscountPlanTranslation> findByDiscountPlanIdAndLanguageId(Long id, long languageId);

}
