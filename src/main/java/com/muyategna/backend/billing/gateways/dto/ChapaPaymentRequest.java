package com.muyategna.backend.billing.gateways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapaPaymentRequest extends BasePaymentRequest {
    private String amount;
    private String currency;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private String txRef;
    private String callbackUrl;
    private String returnUrl;
    private String customizationTitle;
    private String customizationDescription;
}
