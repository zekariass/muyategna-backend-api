package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility.SubscriptionPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.entity.*;
import com.muyategna.backend.billing.mapper.SubscriptionPlanDiscountEligibilityMapper;
import com.muyategna.backend.billing.repository.SubscriptionPlanDiscountEligibilityRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SubscriptionPlanDiscountEligibilityServiceImpl implements SubscriptionPlanDiscountEligibilityService {

    private final SubscriptionPlanDiscountEligibilityRepository repository;
    private final SubscriptionPlanTranslationService subscriptionPlanTranslationService;
    private final DiscountPlanTranslationService discountPlanTranslationService;

    @Autowired
    public SubscriptionPlanDiscountEligibilityServiceImpl(SubscriptionPlanDiscountEligibilityRepository repository,
                                                          SubscriptionPlanTranslationService subscriptionPlanTranslation,
                                                          DiscountPlanTranslationService discountPlanTranslation) {
        this.repository = repository;
        this.subscriptionPlanTranslationService = subscriptionPlanTranslation;
        this.discountPlanTranslationService = discountPlanTranslation;
    }

    @Override
    public SubscriptionPlanDiscountEligibilityDto getSubscriptionPlanDiscountEligibilityById(Long eligibilityId) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        log.info("Retrieving eligibility by id: {}", eligibilityId);
        SubscriptionPlanDiscountEligibility eligibility = repository.findSubscriptionPlanDiscountEligibilityByIdAndCountryId(eligibilityId, countryDto.getId()).orElseThrow(() -> new RuntimeException("Eligibility with id: %d not found".formatted(eligibilityId)));
        log.info("Retrieved eligibility");

        return getSubscriptionPlanDiscountEligibilityDto(eligibility);
    }

    private SubscriptionPlanDiscountEligibilityDto getSubscriptionPlanDiscountEligibilityDto(SubscriptionPlanDiscountEligibility eligibility) {
        DiscountPlan discountPlan = eligibility.getDiscountPlan();
        SubscriptionPlan subscriptionPlan = eligibility.getSubscriptionPlan();

        DiscountPlanTranslation discountPlanTranslation =
                discountPlanTranslationService
                        .getDiscountPlanTranslationByDiscountPlanId(discountPlan.getId())
                        .orElseThrow(() -> new RuntimeException("Discount plan translation not found"));
        SubscriptionPlanTranslation subscriptionPlanTranslation =
                subscriptionPlanTranslationService
                        .getSubscriptionPlanTranslationBySubscriptionPlanId(subscriptionPlan.getId())
                        .orElseThrow(() -> new RuntimeException("Subscription plan translation not found"));

        return SubscriptionPlanDiscountEligibilityMapper.toDto(
                eligibility,
                subscriptionPlanTranslation,
                discountPlanTranslation);
    }


    @Override
    public List<SubscriptionPlanDiscountEligibilityDto> getAllSubscriptionPlanEligibilityBySubscriptionPlanId(Long subscriptionPlanId) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        List<SubscriptionPlanDiscountEligibility> eligibilityList = repository.findAllBySubscriptionPlanIdAndByCountryId(subscriptionPlanId, countryDto.getId());

        List<SubscriptionPlanDiscountEligibilityDto> eligibilityDtoList = new ArrayList<>();
        for (SubscriptionPlanDiscountEligibility eligibility : eligibilityList) {
            eligibilityDtoList.add(getSubscriptionPlanDiscountEligibilityDto(eligibility));
        }

        return eligibilityDtoList;
    }
}
