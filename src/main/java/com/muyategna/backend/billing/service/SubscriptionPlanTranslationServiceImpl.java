package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.SubscriptionPlanTranslation;
import com.muyategna.backend.billing.repository.SubscriptionPlanTranslationRepository;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.system.context.LanguageContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SubscriptionPlanTranslationServiceImpl implements SubscriptionPlanTranslationService {

    private final SubscriptionPlanTranslationRepository repository;
    private final LanguageService languageService;

    @Autowired
    public SubscriptionPlanTranslationServiceImpl(SubscriptionPlanTranslationRepository subscriptionPlanTranslationRepository, LanguageService languageService) {
        this.repository = subscriptionPlanTranslationRepository;
        this.languageService = languageService;
    }

    @Override
    public Optional<SubscriptionPlanTranslation> getSubscriptionPlanTranslationBySubscriptionPlanId(Long subscriptionPlanId) {
        log.info("Retrieving subscription plan translation by subscription plan id: {}", subscriptionPlanId);
        LanguageDto language = LanguageContextHolder.getLanguage();
        Optional<SubscriptionPlanTranslation> subscriptionPlanTranslation = repository.findBySubscriptionPlanIdAndLanguageId(subscriptionPlanId, language.getId());
        if (subscriptionPlanTranslation.isPresent()) {
            return subscriptionPlanTranslation;
        }

        LanguageDto globalLanguage = languageService.getGlobalLanguage();
        subscriptionPlanTranslation = repository.findBySubscriptionPlanIdAndLanguageId(subscriptionPlanId, globalLanguage.getId());
        log.info("Retrieved subscription plan translation by subscription plan id: {}", subscriptionPlanId);
        return subscriptionPlanTranslation;
    }


    @Override
    public List<SubscriptionPlanTranslation> getSubscriptionPlanTranslationsBySubscriptionPlanIds(List<Long> subscriptionPlanIds) {
        log.info("Retrieving subscription plan translations by subscription plan ids");
        LanguageDto language = LanguageContextHolder.getLanguage();

        List<SubscriptionPlanTranslation> userLangTranslations =
                repository.findBySubscriptionPlan_IdInAndLanguage_Id(subscriptionPlanIds, language.getId());

        Map<Long, SubscriptionPlanTranslation> resultMap = new HashMap<>();
        for (SubscriptionPlanTranslation t : userLangTranslations) {
            resultMap.put(t.getSubscriptionPlan().getId(), t);
        }

        // Determine which plans are missing
        List<Long> missingIds = subscriptionPlanIds.stream()
                .filter(id -> !resultMap.containsKey(id))
                .toList();

        if (!missingIds.isEmpty()) {
            List<SubscriptionPlanTranslation> fallbackTranslations =
                    repository.findBySubscriptionPlan_IdInAndLanguage_Id(missingIds, languageService.getGlobalLanguage().getId());

            for (SubscriptionPlanTranslation t : fallbackTranslations) {
                resultMap.putIfAbsent(t.getSubscriptionPlan().getId(), t); // do not overwrite
            }
        }

        log.info("Retrieved subscription plan translations by subscription plan ids");
        return resultMap.values().stream().filter(Objects::nonNull).toList();
    }

}
