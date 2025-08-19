package com.muyategna.backend.billing.gateways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PesapalPaymentRequest extends BasePaymentRequest {
    private String id;
    private String currency;
    private BigDecimal amount;
    private String description;
    private String redirect_mode;
    private String callback_url;
    private String cancellation_url;
    private String notification_id;
    private String branch;
    private BillingAddress billing_address;
}
