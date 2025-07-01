package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.AddOnPlanTranslation;
import com.muyategna.backend.billing.repository.AddOnPlanTranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddOnPlanTranslationServiceImpl implements AddOnPlanTranslationService {

    private final AddOnPlanTranslationRepository addOnPlanTranslationRepository;

    @Autowired
    public AddOnPlanTranslationServiceImpl(AddOnPlanTranslationRepository addOnPlanTranslationRepository) {
        this.addOnPlanTranslationRepository = addOnPlanTranslationRepository;
    }

    @Override
    public List<AddOnPlanTranslation> findTranslationsByAddOnPlanIdInAndLanguageId(List<Long> addOnPlanIds, Long languageId) {
        log.info("Retrieving translations by addOnPlanIds");
        List<AddOnPlanTranslation> translations = addOnPlanTranslationRepository.findByAddOnPlan_IdInAndLanguage_Id(addOnPlanIds, languageId);
        log.info("Retrieved translations of size: {}", translations.size());
        return translations;
    }

    @Override
    public Optional<AddOnPlanTranslation> getAddOnPlanTranslationByAddOnPlanId(Long addOnPlanId) {
        log.info("Retrieving translation by addOnPlanId: {}", addOnPlanId);
        Optional<AddOnPlanTranslation> translation = addOnPlanTranslationRepository.findByAddOnPlan_Id(addOnPlanId);
        log.info("Retrieved translation by addOnPlanId: {}", addOnPlanId);
        return translation;
    }

}
