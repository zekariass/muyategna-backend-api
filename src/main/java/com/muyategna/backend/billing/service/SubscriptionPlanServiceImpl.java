package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
import com.muyategna.backend.billing.entity.SubscriptionPlan;
import com.muyategna.backend.billing.entity.SubscriptionPlanTranslation;
import com.muyategna.backend.billing.mapper.SubscriptionPlanMapper;
import com.muyategna.backend.billing.repository.SubscriptionPlanRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanTranslationService subscriptionPlanTranslationService;

    @Autowired
    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionPlanTranslationService subscriptionPlanTranslationService) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanTranslationService = subscriptionPlanTranslationService;
    }

    @Override
    public List<SubscriptionPlanLocalizedDto> getAllSubscriptionPlansForCurrentCountry() {
        log.info("Retrieving all subscription plans for current country");
        CountryDto countryDto = CountryContextHolder.getCountry();
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findSubscriptionPlansByCountryId(countryDto.getId());

        List<Long> subscriptionPlanIds = subscriptionPlans.stream().map(SubscriptionPlan::getId).toList();
        List<SubscriptionPlanTranslation> subscriptionPlanTranslations = subscriptionPlanTranslationService.getSubscriptionPlanTranslationsBySubscriptionPlanIds(subscriptionPlanIds);

        log.info("Retrieved all subscription plans for current country");
        return SubscriptionPlanMapper.toLocalizedDtoList(subscriptionPlans, subscriptionPlanTranslations);
    }

    @Override
    public SubscriptionPlanLocalizedDto getSubscriptionPlanById(Long subscriptionPlanId) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findSubscriptionPlanByIdAndCountryId(subscriptionPlanId, countryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Subscription plan not found"));

        SubscriptionPlanTranslation translation = subscriptionPlanTranslationService.getSubscriptionPlanTranslationBySubscriptionPlanId(subscriptionPlan.getId()).orElseThrow(() -> new ResourceNotFoundException("Subscription plan not found"));

        return SubscriptionPlanMapper.toLocalizedDto(subscriptionPlan, translation);
    }
}
