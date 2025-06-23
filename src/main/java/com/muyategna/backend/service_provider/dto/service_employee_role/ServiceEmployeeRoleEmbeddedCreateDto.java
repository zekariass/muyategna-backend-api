package com.muyategna.backend.service_provider.dto.service_employee_role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEmployeeRoleEmbeddedCreateDto {

    @NotNull(message = "Service Provider Role ID is required")
    private Long serviceProviderRoleId;

}
