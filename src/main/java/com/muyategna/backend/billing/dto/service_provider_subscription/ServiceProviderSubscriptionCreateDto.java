package com.muyategna.backend.billing.dto.service_provider_subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderSubscriptionCreateDto {
    private Long id;
    private Long providerId;
    private Long subscriptionPlanId;
}
