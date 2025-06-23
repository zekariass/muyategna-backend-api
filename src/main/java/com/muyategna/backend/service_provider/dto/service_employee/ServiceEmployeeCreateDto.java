package com.muyategna.backend.service_provider.dto.service_employee;

import com.muyategna.backend.service_provider.dto.service_employee_role.ServiceEmployeeRoleEmbeddedCreateDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEmployeeCreateDto {
    @NotNull(message = "User Profile ID is required")
    private Long userProfileId;

    @NotNull(message = "Service Provider ID is required")
    private Long serviceProviderId;
    private Boolean isActive;
    private Boolean isBlocked;

    private ServiceEmployeeRoleEmbeddedCreateDto serviceEmployeeRole;
}
