package com.muyategna.backend.service_provider.dto.service_provider_tax_info;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderTaxInfoCreateDto {

    @NotNull(message = "Service Provider ID is required")
    private Long serviceProviderId;

    private String taxPayerIdNumber;

    private Boolean isVatRegistered = false;

    private Boolean isTaxExempt = false;

    private String taxExemptCertificateNumber;

    private String taxExemptionReason;

    private String incomeTaxClassification;

    private Boolean isActive = true;
}
