package com.muyategna.backend.service_provider.dto.service_employee;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEmployeeUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Service ID is required")
    private Long serviceProviderId;

    @NotNull(message = "User Profile ID is required")
    private Long userProfileId;

    private Boolean isActive;
    private Boolean isBlocked;
}
