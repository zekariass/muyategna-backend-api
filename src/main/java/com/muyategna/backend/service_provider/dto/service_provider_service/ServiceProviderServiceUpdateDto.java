package com.muyategna.backend.service_provider.dto.service_provider_service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderServiceUpdateDto {
    @NotNull(message = "ID is required")
    private Long id;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Provider ID is required")
    private Long providerId;
    private Boolean isActive;
}
