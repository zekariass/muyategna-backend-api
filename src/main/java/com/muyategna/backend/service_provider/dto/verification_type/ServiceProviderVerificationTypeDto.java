package com.muyategna.backend.service_provider.dto.verification_type;

import com.muyategna.backend.service_provider.enums.ServiceProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderVerificationTypeDto {
    private Long id;
    private String name;
    private ServiceProviderType providerType;
    private Boolean isMandatory;
    private Boolean documentRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}