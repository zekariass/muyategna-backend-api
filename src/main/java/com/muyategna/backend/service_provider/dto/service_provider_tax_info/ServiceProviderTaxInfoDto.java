package com.muyategna.backend.service_provider.dto.service_provider_tax_info;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderTaxInfoDto {

    private Long id;

    private Long serviceProviderId;

    private String taxPayerIdNumber;

    private Boolean isVatRegistered;

    private Boolean isTaxExempt;

    private String taxExemptCertificateNumber;

    private String taxExemptionReason;

    private String incomeTaxClassification;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
