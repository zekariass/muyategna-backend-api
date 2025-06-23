package com.muyategna.backend.service_provider.dto.service_provider_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderServiceDto {
    private Long id;
    private Long serviceId;
    private Long providerId;
    private Boolean isActive;
    private LocalDateTime linkedAt;
    private LocalDateTime updatedAt;
}
