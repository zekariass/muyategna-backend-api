package com.muyategna.backend.billing.dto.service_provider_coupon_tracker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderCouponTrackerUpdateDto {
    private Long id;
    private Long couponId;
    private Long providerId;
    private Integer usageLimit;
    private Integer useCount;
    private Boolean isActive;
}

