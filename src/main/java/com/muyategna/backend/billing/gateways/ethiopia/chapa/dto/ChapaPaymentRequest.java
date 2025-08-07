package com.muyategna.backend.billing.gateways.ethiopia.chapa.dto;

import lombok.Data;

@Data
public class ChapaPaymentRequest {
    private String amount;
    private String currency;
    private String email;
    private String first_name;
    private String last_name;
    private String tx_ref;
    private String callback_url;
    private String return_url;
    private String customization_title;
    private String customization_description;
}
