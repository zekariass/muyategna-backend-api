package com.muyategna.backend.system.mapper;

import com.muyategna.backend.system.dto.SystemConfigCreateDto;
import com.muyategna.backend.system.dto.SystemConfigDto;
import com.muyategna.backend.system.dto.SystemConfigUpdateDto;
import com.muyategna.backend.system.entity.SystemConfig;

public final class SystemConfigMapper {

    public static SystemConfigDto toDto(SystemConfig systemConfig) {
        return SystemConfigDto.builder()
                .id(systemConfig.getId())
                .name(systemConfig.getName())
                .value(systemConfig.getValue())
                .type(systemConfig.getType())
                .createdBy(systemConfig.getCreatedBy())
                .updatedBy(systemConfig.getUpdatedBy())
                .createdAt(systemConfig.getCreatedAt())
                .updatedAt(systemConfig.getUpdatedAt())
                .build();
    }


    public static SystemConfigCreateDto toCreateDto(SystemConfig systemConfig) {
        return SystemConfigCreateDto.builder()
                .name(systemConfig.getName())
                .value(systemConfig.getValue())
                .type(systemConfig.getType())
                .build();
    }


    public static SystemConfigUpdateDto toUpdateDto(SystemConfig systemConfig) {
        return SystemConfigUpdateDto.builder()
                .id(systemConfig.getId())
                .name(systemConfig.getName())
                .value(systemConfig.getValue())
                .type(systemConfig.getType())
                .build();
    }

}
