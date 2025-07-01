package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import com.muyategna.backend.billing.dto.service_provider_add_on.ServiceProviderAddOnDto;
import com.muyategna.backend.billing.entity.ServiceProviderAddOn;

public final class ServiceProviderAddOnMapper {

    public static ServiceProviderAddOnDto toDto(ServiceProviderAddOn serviceProviderAddOn,
                                                AddOnPlanLocalizedDto addOnPlanLocalizedDto) {
        return ServiceProviderAddOnDto.builder()
                .id(serviceProviderAddOn.getId())
                .providerId(serviceProviderAddOn.getProvider().getId())
                .addOnPlan(addOnPlanLocalizedDto)
                .initialCredits(serviceProviderAddOn.getInitialCredits())
                .usedCredits(serviceProviderAddOn.getUsedCredits())
                .purchasedAt(serviceProviderAddOn.getPurchasedAt())
                .isActive(serviceProviderAddOn.getIsActive())
                .expiresAt(serviceProviderAddOn.getExpiresAt())
                .build();
    }
}
