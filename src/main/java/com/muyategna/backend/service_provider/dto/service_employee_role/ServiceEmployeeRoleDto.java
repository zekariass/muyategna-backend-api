package com.muyategna.backend.service_provider.dto.service_employee_role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEmployeeRoleDto {

    private Long id;
    private Long serviceProviderRoleId;
    private Long employeeId;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
}
