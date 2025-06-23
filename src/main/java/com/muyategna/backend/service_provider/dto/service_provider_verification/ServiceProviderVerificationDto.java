package com.muyategna.backend.service_provider.dto.service_provider_verification;

import com.muyategna.backend.service_provider.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderVerificationDto {
    private Long id;
    private Long providerId;
    private Long typeId;
    private VerificationStatus verificationStatus;
    private String documentUrl;
    private String reasonForRejection;
    private String verificationNote;
    private Long verifiedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}