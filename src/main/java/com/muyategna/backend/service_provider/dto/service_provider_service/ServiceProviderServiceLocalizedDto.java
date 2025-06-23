package com.muyategna.backend.service_provider.dto.service_provider_service;

import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderServiceLocalizedDto {
    private Long id;
    private ServiceLocalizedDto service;
    private Long providerId;
    private Boolean isActive;
    private LocalDateTime linkedAt;
    private LocalDateTime updatedAt;
}
