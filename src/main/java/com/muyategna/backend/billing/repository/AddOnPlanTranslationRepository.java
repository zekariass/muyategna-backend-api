package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.AddOnPlanTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddOnPlanTranslationRepository extends JpaRepository<AddOnPlanTranslation, Long> {

    List<AddOnPlanTranslation> findByAddOnPlan_IdInAndLanguage_Id(List<Long> addOnPlanIds, Long languageId);

    Optional<AddOnPlanTranslation> findByAddOnPlan_Id(Long addOnPlanId);
}
