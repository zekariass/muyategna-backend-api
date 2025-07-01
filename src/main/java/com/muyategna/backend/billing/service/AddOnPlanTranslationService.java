package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.AddOnPlanTranslation;

import java.util.List;
import java.util.Optional;

public interface AddOnPlanTranslationService {
    List<AddOnPlanTranslation> findTranslationsByAddOnPlanIdInAndLanguageId(List<Long> addOnPlanIds, Long languageId);

    Optional<AddOnPlanTranslation> getAddOnPlanTranslationByAddOnPlanId(Long addOnPlanId);
}
