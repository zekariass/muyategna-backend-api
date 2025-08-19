package com.muyategna.backend.billing.gateways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferRequest extends BasePaymentRequest {
    private String txRef;
    private String callbackUrl;
    private String returnUrl;
    private String customizationTitle;
    private String customizationDescription;
}
