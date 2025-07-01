package com.muyategna.backend.billing.dto.service_provider_coupon_tracker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderCouponTrackerDto {
    private Long id;
    private Long couponId;
    private Long providerId;
    private Integer usageLimit;
    private Integer useCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

