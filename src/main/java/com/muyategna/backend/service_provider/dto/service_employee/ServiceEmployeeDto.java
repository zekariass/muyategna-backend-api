package com.muyategna.backend.service_provider.dto.service_employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEmployeeDto {
    private Long id;
    private Long userProfileId;
    private Long serviceProviderId;
    private Boolean isActive;
    private Boolean isBlocked;
    private Instant createdAt;
    private Instant updatedAt;
}
