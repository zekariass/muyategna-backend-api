package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanCreateDto;
import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanDto;
import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanUpdateDto;
import com.muyategna.backend.billing.entity.SubscriptionPlan;
import com.muyategna.backend.billing.entity.SubscriptionPlanTranslation;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.professional_service.entity.ServiceCategory;
import com.muyategna.backend.system.exception.ResourceNotFoundException;

import java.util.List;

public final class SubscriptionPlanMapper {

    public static SubscriptionPlanDto toDto(SubscriptionPlan subscriptionPlan) {
        return SubscriptionPlanDto.builder()
                .id(subscriptionPlan.getId())
                .countryId(subscriptionPlan.getCountry().getId())
                .name(subscriptionPlan.getName())
                .priceAmount(subscriptionPlan.getPriceAmount())
                .billingCycle(subscriptionPlan.getBillingCycle())
                .billingCycleCount(subscriptionPlan.getBillingCycleCount())
                .trialPeriodDays(subscriptionPlan.getTrialPeriodDays())
                .creditsIncluded(subscriptionPlan.getCreditsIncluded())
                .sortOrder(subscriptionPlan.getSortOrder())
                .isDefault(subscriptionPlan.getIsDefault())
                .isActive(subscriptionPlan.getIsActive())
                .slug(subscriptionPlan.getSlug())
                .createdAt(subscriptionPlan.getCreatedAt())
                .updatedAt(subscriptionPlan.getUpdatedAt())
                .build();
    }


    public static SubscriptionPlanLocalizedDto toLocalizedDto(SubscriptionPlan subscriptionPlan,
                                                              SubscriptionPlanTranslation translation) {
        return SubscriptionPlanLocalizedDto.builder()
                .id(subscriptionPlan.getId())
                .countryId(subscriptionPlan.getCountry().getId())
                .displayName(translation.getDisplayName())
                .description(translation.getDescription())
                .priceAmount(subscriptionPlan.getPriceAmount())
                .billingCycle(subscriptionPlan.getBillingCycle())
                .billingCycleCount(subscriptionPlan.getBillingCycleCount())
                .trialPeriodDays(subscriptionPlan.getTrialPeriodDays())
                .creditsIncluded(subscriptionPlan.getCreditsIncluded())
                .sortOrder(subscriptionPlan.getSortOrder())
                .isDefault(subscriptionPlan.getIsDefault())
                .isActive(subscriptionPlan.getIsActive())
                .slug(subscriptionPlan.getSlug())
                .build();
    }


    public static SubscriptionPlanLocalizedDto toLocalizedMinimalDto(SubscriptionPlan subscriptionPlan,
                                                                     SubscriptionPlanTranslation translation) {
        return SubscriptionPlanLocalizedDto.builder()
                .id(subscriptionPlan.getId())
                .countryId(subscriptionPlan.getCountry().getId())
                .displayName(translation.getDisplayName())
                .description(translation.getDescription())
                .priceAmount(subscriptionPlan.getPriceAmount())
                .billingCycle(subscriptionPlan.getBillingCycle())
                .billingCycleCount(subscriptionPlan.getBillingCycleCount())
                .trialPeriodDays(subscriptionPlan.getTrialPeriodDays())
                .creditsIncluded(subscriptionPlan.getCreditsIncluded())
                .sortOrder(subscriptionPlan.getSortOrder())
                .isDefault(subscriptionPlan.getIsDefault())
                .isActive(subscriptionPlan.getIsActive())
                .slug(subscriptionPlan.getSlug())
                .build();
    }


    public static List<SubscriptionPlanLocalizedDto> toLocalizedDtoList(
            List<SubscriptionPlan> subscriptionPlans,
            List<SubscriptionPlanTranslation> translations) {

        return subscriptionPlans.stream()
                .map(subscriptionPlan -> {
                    SubscriptionPlanTranslation translation = translations.stream()
                            .filter(t -> t.getSubscriptionPlan().getId().equals(subscriptionPlan.getId()))
                            .findFirst()
                            .orElse(null);

                    if (translation == null) {
                        throw new ResourceNotFoundException("Subscription plan not found for one of the subscription plan translation");
                    }
                    return toLocalizedDto(subscriptionPlan, translation);
                })
                .toList();
    }


    public static SubscriptionPlan toEntity(SubscriptionPlanDto subscriptionPlanDto,
                                            Country country,
                                            ServiceCategory serviceCategory) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setId(subscriptionPlanDto.getId());
        subscriptionPlan.setCountry(country);
        subscriptionPlan.setName(subscriptionPlanDto.getName());
        subscriptionPlan.setPriceAmount(subscriptionPlanDto.getPriceAmount());
        subscriptionPlan.setBillingCycle(subscriptionPlanDto.getBillingCycle());
        subscriptionPlan.setBillingCycleCount(subscriptionPlanDto.getBillingCycleCount());
        subscriptionPlan.setTrialPeriodDays(subscriptionPlanDto.getTrialPeriodDays());
        subscriptionPlan.setCreditsIncluded(subscriptionPlanDto.getCreditsIncluded());
        subscriptionPlan.setSortOrder(subscriptionPlanDto.getSortOrder());
        subscriptionPlan.setIsDefault(subscriptionPlanDto.getIsDefault());
        subscriptionPlan.setIsActive(subscriptionPlanDto.getIsActive());
        subscriptionPlan.setSlug(subscriptionPlanDto.getSlug());
        subscriptionPlan.setUpdatedAt(subscriptionPlanDto.getUpdatedAt());
        return subscriptionPlan;
    }


    public static SubscriptionPlan toEntity(SubscriptionPlanUpdateDto subscriptionPlanDto,
                                            Country country,
                                            ServiceCategory serviceCategory) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setId(subscriptionPlanDto.getId());
        subscriptionPlan.setCountry(country);
        subscriptionPlan.setName(subscriptionPlanDto.getName());
        subscriptionPlan.setPriceAmount(subscriptionPlanDto.getPriceAmount());
        subscriptionPlan.setBillingCycle(subscriptionPlanDto.getBillingCycle());
        subscriptionPlan.setBillingCycleCount(subscriptionPlanDto.getBillingCycleCount());
        subscriptionPlan.setTrialPeriodDays(subscriptionPlanDto.getTrialPeriodDays());
        subscriptionPlan.setCreditsIncluded(subscriptionPlanDto.getCreditsIncluded());
        subscriptionPlan.setSortOrder(subscriptionPlanDto.getSortOrder());
        subscriptionPlan.setIsDefault(subscriptionPlanDto.getIsDefault());
        subscriptionPlan.setIsActive(subscriptionPlanDto.getIsActive());
        subscriptionPlan.setSlug(subscriptionPlanDto.getSlug());
        return subscriptionPlan;
    }


    public static SubscriptionPlan toEntity(SubscriptionPlanCreateDto subscriptionPlanDto,
                                            Country country,
                                            ServiceCategory serviceCategory) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setCountry(country);
        subscriptionPlan.setName(subscriptionPlanDto.getName());
        subscriptionPlan.setPriceAmount(subscriptionPlanDto.getPriceAmount());
        subscriptionPlan.setBillingCycle(subscriptionPlanDto.getBillingCycle());
        subscriptionPlan.setBillingCycleCount(subscriptionPlanDto.getBillingCycleCount());
        subscriptionPlan.setTrialPeriodDays(subscriptionPlanDto.getTrialPeriodDays());
        subscriptionPlan.setCreditsIncluded(subscriptionPlanDto.getCreditsIncluded());
        subscriptionPlan.setSortOrder(subscriptionPlanDto.getSortOrder());
        subscriptionPlan.setIsDefault(subscriptionPlanDto.getIsDefault());
        subscriptionPlan.setIsActive(subscriptionPlanDto.getIsActive());
        subscriptionPlan.setSlug(subscriptionPlanDto.getSlug());
        return subscriptionPlan;
    }
}
