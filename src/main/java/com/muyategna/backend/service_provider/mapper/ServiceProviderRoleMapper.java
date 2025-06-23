package com.muyategna.backend.service_provider.mapper;

import com.muyategna.backend.service_provider.dto.service_provider_role.ServiceProviderRoleCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_role.ServiceProviderRoleDto;
import com.muyategna.backend.service_provider.dto.service_provider_role.ServiceProviderRoleUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderRole;

public final class ServiceProviderRoleMapper {

    public static ServiceProviderRoleDto toDto(ServiceProviderRole serviceProviderRole) {
        return ServiceProviderRoleDto.builder()
                .id(serviceProviderRole.getId())
                .name(serviceProviderRole.getName())
                .description(serviceProviderRole.getDescription())
                .createdAt(serviceProviderRole.getCreatedAt())
                .updatedAt(serviceProviderRole.getUpdatedAt())
                .build();
    }


    public static ServiceProviderRole toEntity(ServiceProviderRoleDto dto) {
        ServiceProviderRole serviceProviderRole = new ServiceProviderRole();
        serviceProviderRole.setId(dto.getId());
        serviceProviderRole.setName(dto.getName());
        serviceProviderRole.setDescription(dto.getDescription());
        serviceProviderRole.setCreatedAt(dto.getCreatedAt());
        serviceProviderRole.setUpdatedAt(dto.getUpdatedAt());
        return serviceProviderRole;
    }


    public static ServiceProviderRole toEntity(ServiceProviderRoleCreateDto dto) {
        ServiceProviderRole serviceProviderRole = new ServiceProviderRole();
        serviceProviderRole.setName(dto.getName());
        serviceProviderRole.setDescription(dto.getDescription());
        return serviceProviderRole;
    }


    public static ServiceProviderRole toEntity(ServiceProviderRoleUpdateDto dto) {
        ServiceProviderRole serviceProviderRole = new ServiceProviderRole();
        serviceProviderRole.setId(dto.getId());
        serviceProviderRole.setName(dto.getName());
        serviceProviderRole.setDescription(dto.getDescription());
        return serviceProviderRole;
    }
}
