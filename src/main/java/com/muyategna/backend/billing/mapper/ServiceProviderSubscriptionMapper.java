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
                .subscriptionPlan(subscriptionPlanLocalizedDto)
                .initialCredits(serviceProviderSubscription.getInitialCredits())
                .usedCredits(serviceProviderSubscription.getUsedCredits())
                .upgradedFromId(serviceProviderSubscription.getUpgradedFrom().getId())
                .isActive(serviceProviderSubscription.getIsActive())
                .expiresAt(serviceProviderSubscription.getExpiresAt())
                .subscribedAt(serviceProviderSubscription.getSubscribedAt())
                .build();
    }


    public static ServiceProviderSubscription toEntity(ServiceProviderSubscriptionDto serviceProviderSubscriptionDto,
                                                       ServiceProvider serviceProvider,
                                                       SubscriptionPlan subscriptionPlan) {
        ServiceProviderSubscription serviceProviderSubscription = new ServiceProviderSubscription();
        serviceProviderSubscription.setId(serviceProviderSubscriptionDto.getId());
        serviceProviderSubscription.setProvider(serviceProvider);
        serviceProviderSubscription.setSubscriptionPlan(subscriptionPlan);
        serviceProviderSubscription.setInitialCredits(serviceProviderSubscriptionDto.getInitialCredits());
        serviceProviderSubscription.setUsedCredits(serviceProviderSubscriptionDto.getUsedCredits());
        serviceProviderSubscription.setIsActive(serviceProviderSubscriptionDto.getIsActive());
        serviceProviderSubscription.setExpiresAt(serviceProviderSubscriptionDto.getExpiresAt());
        serviceProviderSubscription.setSubscribedAt(serviceProviderSubscriptionDto.getSubscribedAt());
        return serviceProviderSubscription;
    }


    public static ServiceProviderSubscription toEntity(ServiceProviderSubscriptionCreateDto serviceProviderSubscriptionDto,
                                                       ServiceProvider serviceProvider,
                                                       SubscriptionPlan subscriptionPlan) {
        ServiceProviderSubscription serviceProviderSubscription = new ServiceProviderSubscription();
        serviceProviderSubscription.setId(serviceProviderSubscriptionDto.getId());
        serviceProviderSubscription.setProvider(serviceProvider);
        serviceProviderSubscription.setSubscriptionPlan(subscriptionPlan);
        serviceProviderSubscription.setInitialCredits(serviceProviderSubscriptionDto.getInitialCredits());
        serviceProviderSubscription.setUsedCredits(serviceProviderSubscriptionDto.getUsedCredits());
        serviceProviderSubscription.setIsActive(serviceProviderSubscriptionDto.getIsActive());
        serviceProviderSubscription.setExpiresAt(serviceProviderSubscriptionDto.getExpiresAt());
        serviceProviderSubscription.setSubscribedAt(serviceProviderSubscriptionDto.getSubscribedAt());
        return serviceProviderSubscription;
    }
}
