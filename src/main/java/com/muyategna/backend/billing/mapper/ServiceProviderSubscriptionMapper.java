package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.service_provider_subscription.ServiceProviderSubscriptionCreateDto;
import com.muyategna.backend.billing.dto.service_provider_subscription.ServiceProviderSubscriptionDto;
import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.SubscriptionPlan;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

public final class ServiceProviderSubscriptionMapper {

    public static ServiceProviderSubscriptionDto toDto(ServiceProviderSubscription serviceProviderSubscription,
                                                       SubscriptionPlanLocalizedDto subscriptionPlanLocalizedDto) {
        return ServiceProviderSubscriptionDto.builder()
                .id(serviceProviderSubscription.getId())
                .providerId(serviceProviderSubscription.getProvider().getId())
                .subscriptionPlanId(serviceProviderSubscription.getSubscriptionPlan().getId())
                .renewalStatus(serviceProviderSubscription.getRenewalStatus())
                .nextBillingDate(serviceProviderSubscription.getNextBillingDate())
                .cancelledAt(serviceProviderSubscription.getCancelledAt())
                .renewalAttemptedAt(serviceProviderSubscription.getRenewalAttemptedAt())
                .startedAt(serviceProviderSubscription.getStartedAt())
                .endDate(serviceProviderSubscription.getEndDate())
                .trialPeriodStartedAt(serviceProviderSubscription.getTrialPeriodStartedAt())
                .trialPeriodEndsAt(serviceProviderSubscription.getTrialPeriodEndsAt())
                .gracePeriodStartedAt(serviceProviderSubscription.getGracePeriodStartedAt())
                .gracePeriodEndsAt(serviceProviderSubscription.getGracePeriodEndsAt())
                .extraData(serviceProviderSubscription.getExtraData())
                .isActive(serviceProviderSubscription.getIsActive())
                .initialCredits(serviceProviderSubscription.getInitialCredits())
                .balance(serviceProviderSubscription.getBalance())
                .autoRenew(serviceProviderSubscription.getAutoRenew())
                .lastBillingDate(serviceProviderSubscription.getLastBillingDate())
                .createdAt(serviceProviderSubscription.getCreatedAt())
                .updatedAt(serviceProviderSubscription.getUpdatedAt())
                .upgradedFromId(serviceProviderSubscription.getUpgradedFrom().getId())
                .downgradedFromId(serviceProviderSubscription.getDowngradedFrom().getId())
                .build();
    }

    public static ServiceProviderSubscriptionDto toDto(ServiceProviderSubscription serviceProviderSubscription) {
        return ServiceProviderSubscriptionDto.builder()
                .id(serviceProviderSubscription.getId())
                .providerId(serviceProviderSubscription.getProvider().getId())
                .subscriptionPlanId(serviceProviderSubscription.getSubscriptionPlan().getId())
                .renewalStatus(serviceProviderSubscription.getRenewalStatus())
                .nextBillingDate(serviceProviderSubscription.getNextBillingDate())
                .cancelledAt(serviceProviderSubscription.getCancelledAt())
                .renewalAttemptedAt(serviceProviderSubscription.getRenewalAttemptedAt())
                .startedAt(serviceProviderSubscription.getStartedAt())
                .endDate(serviceProviderSubscription.getEndDate())
                .trialPeriodStartedAt(serviceProviderSubscription.getTrialPeriodStartedAt())
                .trialPeriodEndsAt(serviceProviderSubscription.getTrialPeriodEndsAt())
                .gracePeriodStartedAt(serviceProviderSubscription.getGracePeriodStartedAt())
                .gracePeriodEndsAt(serviceProviderSubscription.getGracePeriodEndsAt())
                .extraData(serviceProviderSubscription.getExtraData())
                .isActive(serviceProviderSubscription.getIsActive())
                .initialCredits(serviceProviderSubscription.getInitialCredits())
                .balance(serviceProviderSubscription.getBalance())
                .autoRenew(serviceProviderSubscription.getAutoRenew())
                .lastBillingDate(serviceProviderSubscription.getLastBillingDate())
                .createdAt(serviceProviderSubscription.getCreatedAt())
                .updatedAt(serviceProviderSubscription.getUpdatedAt())
                .upgradedFromId(serviceProviderSubscription.getUpgradedFrom().getId())
                .downgradedFromId(serviceProviderSubscription.getDowngradedFrom().getId())
                .build();
    }


    public static ServiceProviderSubscription toEntity(ServiceProviderSubscriptionDto serviceProviderSubscriptionDto,
                                                       ServiceProvider serviceProvider,
                                                       SubscriptionPlan subscriptionPlan) {
        ServiceProviderSubscription serviceProviderSubscription = new ServiceProviderSubscription();
        serviceProviderSubscription.setId(serviceProviderSubscriptionDto.getId());
        serviceProviderSubscription.setProvider(serviceProvider);
        serviceProviderSubscription.setSubscriptionPlan(subscriptionPlan);
        serviceProviderSubscription.setRenewalStatus(serviceProviderSubscriptionDto.getRenewalStatus());
        serviceProviderSubscription.setNextBillingDate(serviceProviderSubscriptionDto.getNextBillingDate());
        serviceProviderSubscription.setCancelledAt(serviceProviderSubscriptionDto.getCancelledAt());
        serviceProviderSubscription.setRenewalAttemptedAt(serviceProviderSubscriptionDto.getRenewalAttemptedAt());
        serviceProviderSubscription.setStartedAt(serviceProviderSubscriptionDto.getStartedAt());
        serviceProviderSubscription.setEndDate(serviceProviderSubscriptionDto.getEndDate());
        serviceProviderSubscription.setTrialPeriodStartedAt(serviceProviderSubscriptionDto.getTrialPeriodStartedAt());
        serviceProviderSubscription.setTrialPeriodEndsAt(serviceProviderSubscriptionDto.getTrialPeriodEndsAt());
        serviceProviderSubscription.setGracePeriodStartedAt(serviceProviderSubscriptionDto.getGracePeriodStartedAt());
        serviceProviderSubscription.setGracePeriodEndsAt(serviceProviderSubscriptionDto.getGracePeriodEndsAt());
        serviceProviderSubscription.setExtraData(serviceProviderSubscriptionDto.getExtraData());
        serviceProviderSubscription.setIsActive(serviceProviderSubscriptionDto.getIsActive());
        serviceProviderSubscription.setInitialCredits(serviceProviderSubscriptionDto.getInitialCredits());
        serviceProviderSubscription.setBalance(serviceProviderSubscriptionDto.getBalance());
        serviceProviderSubscription.setAutoRenew(serviceProviderSubscriptionDto.getAutoRenew());
        serviceProviderSubscription.setLastBillingDate(serviceProviderSubscriptionDto.getLastBillingDate());
        serviceProviderSubscription.setCreatedAt(serviceProviderSubscriptionDto.getCreatedAt());
        return serviceProviderSubscription;
    }


    public static ServiceProviderSubscription toEntity(ServiceProviderSubscriptionCreateDto serviceProviderSubscriptionDto,
                                                       ServiceProvider serviceProvider,
                                                       SubscriptionPlan subscriptionPlan) {
        ServiceProviderSubscription serviceProviderSubscription = new ServiceProviderSubscription();
        serviceProviderSubscription.setId(serviceProviderSubscriptionDto.getId());
        serviceProviderSubscription.setProvider(serviceProvider);
        serviceProviderSubscription.setSubscriptionPlan(subscriptionPlan);

        return serviceProviderSubscription;
    }


    public static ServiceProviderSubscription toBasicEntity(ServiceProviderSubscriptionDto serviceProviderSubscriptionDto) {
        ServiceProviderSubscription serviceProviderSubscription = new ServiceProviderSubscription();
        serviceProviderSubscription.setId(serviceProviderSubscriptionDto.getId()); // Assuming ID is generated by the database
        return serviceProviderSubscription;
    }
}
