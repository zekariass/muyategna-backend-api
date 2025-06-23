package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.service_provider.dto.service_employee_role.ServiceEmployeeRoleCreateDto;
import com.muyategna.backend.service_provider.dto.service_employee_role.ServiceEmployeeRoleDto;
import com.muyategna.backend.service_provider.dto.service_employee_role.ServiceEmployeeRoleEmbeddedCreateDto;
import com.muyategna.backend.service_provider.dto.service_employee_role.ServiceEmployeeRoleUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceEmployee;
import com.muyategna.backend.service_provider.entity.ServiceEmployeeRole;
import com.muyategna.backend.service_provider.entity.ServiceProviderRole;

public final class ServiceEmployeeRoleMapper {

    public static ServiceEmployeeRoleDto toDto(ServiceEmployeeRole serviceEmployeeRole) {
        if (serviceEmployeeRole == null) {
            return null;
        }
        return ServiceEmployeeRoleDto.builder()
                .id(serviceEmployeeRole.getId())
                .serviceProviderRoleId(serviceEmployeeRole.getRole().getId())
                .employeeId(serviceEmployeeRole.getEmployee().getId())
                .assignedAt(serviceEmployeeRole.getAssignedAt())
                .updatedAt(serviceEmployeeRole.getUpdatedAt())
                .build();
    }


    public static ServiceEmployeeRole toEntity(ServiceEmployeeRoleDto dto,
                                               ServiceProviderRole serviceProviderRole,
                                               ServiceEmployee employee) {
        if (dto == null) {
            return null;
        }
        ServiceEmployeeRole serviceEmployeeRole = new ServiceEmployeeRole();
        serviceEmployeeRole.setId(dto.getId());
        serviceEmployeeRole.setAssignedAt(dto.getAssignedAt());
        serviceEmployeeRole.setUpdatedAt(dto.getUpdatedAt());
        serviceEmployeeRole.setRole(serviceProviderRole);
        serviceEmployeeRole.setEmployee(employee);
        return serviceEmployeeRole;
    }


    public static ServiceEmployeeRole toEntity(ServiceEmployeeRoleCreateDto dto,
                                               ServiceProviderRole serviceProviderRole,
                                               ServiceEmployee employee) {
        if (dto == null) {
            return null;
        }
        ServiceEmployeeRole serviceEmployeeRole = new ServiceEmployeeRole();
        serviceEmployeeRole.setRole(serviceProviderRole);
        serviceEmployeeRole.setEmployee(employee);
        return serviceEmployeeRole;
    }


    public static ServiceEmployeeRole toEntity(ServiceEmployeeRoleEmbeddedCreateDto dto,
                                               ServiceProviderRole serviceProviderRole,
                                               ServiceEmployee employee) {
        if (dto == null) {
            return null;
        }
        ServiceEmployeeRole serviceEmployeeRole = new ServiceEmployeeRole();
        serviceEmployeeRole.setRole(serviceProviderRole);
        serviceEmployeeRole.setEmployee(employee);
        return serviceEmployeeRole;
    }


    public static ServiceEmployeeRole toEntity(ServiceEmployeeRoleUpdateDto dto,
                                               ServiceProviderRole serviceProviderRole,
                                               ServiceEmployee employee) {
        if (dto == null) {
            return null;
        }
        ServiceEmployeeRole serviceEmployeeRole = new ServiceEmployeeRole();
        serviceEmployeeRole.setId(dto.getId());
        serviceEmployeeRole.setRole(serviceProviderRole);
        serviceEmployeeRole.setEmployee(employee);
        return serviceEmployeeRole;
    }
}
