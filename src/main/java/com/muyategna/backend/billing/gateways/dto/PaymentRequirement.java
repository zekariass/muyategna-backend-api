package com.muyategna.backend.billing.gateways.dto;

import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.enums.UserEntityType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequirement {

    @NotNull(message = "Product type is required")
    private ProductType productType;

    @NotNull(message = "Payment method id is required")
    private Long paymentMethodId;
    private Long addOnPlanId;
    private Long subscriptionPlanId;
    private Long discountPlanId;
    private String couponCode;

    @NotNull(message = "User entity id is required")
    private UserEntityType userEntityType;

    @NotNull(message = "User entity id is required")
    private Long userEntityId;
}
