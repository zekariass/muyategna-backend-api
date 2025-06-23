package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeCreateDto;
import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeDto;
import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceEmployee;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.user.entity.UserProfile;

public final class ServiceEmployeeMapper {
    public static ServiceEmployeeDto toDto(ServiceEmployee serviceEmployee) {
        if (serviceEmployee == null) {
            return null;
        }
        return ServiceEmployeeDto.builder()
                .id(serviceEmployee.getId())
                .userProfileId(serviceEmployee.getUserProfile().getId())
                .serviceProviderId(serviceEmployee.getServiceProvider().getId())
                .isActive(serviceEmployee.getIsActive())
                .isBlocked(serviceEmployee.getIsBlocked())
                .createdAt(serviceEmployee.getCreatedAt())
                .updatedAt(serviceEmployee.getUpdatedAt())
                .build();
    }


    public static ServiceEmployee toEntity(ServiceEmployeeDto dto,
                                           ServiceProvider serviceProvider,
                                           UserProfile userProfile) {
        if (dto == null) {
            return null;
        }
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        serviceEmployee.setId(dto.getId());
        serviceEmployee.setIsActive(dto.getIsActive());
        serviceEmployee.setIsBlocked(dto.getIsBlocked());
        serviceEmployee.setUserProfile(userProfile);
        serviceEmployee.setServiceProvider(serviceProvider);
        serviceEmployee.setCreatedAt(dto.getCreatedAt());
        serviceEmployee.setUpdatedAt(dto.getUpdatedAt());
        return serviceEmployee;
    }


    public static ServiceEmployee toEntity(ServiceEmployeeCreateDto dto,
                                           ServiceProvider serviceProvider,
                                           UserProfile userProfile) {
        if (dto == null) {
            return null;
        }
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        serviceEmployee.setIsActive(dto.getIsActive());
        serviceEmployee.setIsBlocked(dto.getIsBlocked());
        serviceEmployee.setUserProfile(userProfile);
        serviceEmployee.setServiceProvider(serviceProvider);
        return serviceEmployee;
    }


    public static ServiceEmployee toEntity(ServiceEmployeeUpdateDto dto,
                                           ServiceProvider serviceProvider,
                                           UserProfile userProfile) {
        if (dto == null) {
            return null;
        }
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        serviceEmployee.setId(dto.getId());
        serviceEmployee.setIsActive(dto.getIsActive());
        serviceEmployee.setIsBlocked(dto.getIsBlocked());
        serviceEmployee.setUserProfile(userProfile);
        serviceEmployee.setServiceProvider(serviceProvider);
        return serviceEmployee;
    }
}
