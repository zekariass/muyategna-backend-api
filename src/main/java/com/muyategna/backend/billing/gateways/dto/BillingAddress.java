package com.muyategna.backend.billing.gateways.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingAddress {
    private String email_address;
    private String phone_number;
    private String country_code;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String line_1;
    private String line_2;
    private String city;
    private String state;
    private Integer postal_code;
    private Integer zip_code;

}
