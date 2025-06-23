package com.muyategna.backend.service_provider.dto.service_provider_agreement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderAgreementUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Country is required")
    private Long documentId;

    @NotNull(message = "Name is required")
    private Long providerId;

    private Boolean isSigned = false;
}