package com.muyategna.backend.billing.dto.coupon;

import com.muyategna.backend.billing.enums.CouponStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUpdateDto {
    private Long id;
    private String code;
    private Integer usageLimit;
    private Integer perUserLimit;
    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;
    private CouponStatusEnum status;
    private Long discountPlanId;
    private Boolean isGlobal;
}
