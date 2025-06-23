package com.muyategna.backend.service_provider.dto.service_provider_verification;

import com.muyategna.backend.service_provider.enums.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderVerificationCreatedDto {
    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Type ID is required")
    private Long typeId;

    @NotNull(message = "Status is required")
    private VerificationStatus verificationStatus;

    private String documentUrl;
    private String reasonForRejection;
    private String verificationNote;
}