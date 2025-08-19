package com.muyategna.backend.billing.gateways.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasePaymentRequest {
    private String productType;
    private Long paymentMethod;
    private Long addOnPlanId;
    private Long discountPlanId;
    private String couponCode;
}
