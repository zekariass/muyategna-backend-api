package com.muyategna.backend.system.service;

import com.muyategna.backend.system.dto.SystemConfigCreateDto;
import com.muyategna.backend.system.dto.SystemConfigDto;
import com.muyategna.backend.system.dto.SystemConfigUpdateDto;
import com.muyategna.backend.system.entity.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SystemConfigService {
    Page<SystemConfigDto> getAllSystemConfigsFromDB(Pageable pageable);

    SystemConfigDto getSystemConfigByIdFromDB(Long systemConfigId);

    SystemConfigDto updateSystemConfig(Long systemConfigId, SystemConfigUpdateDto systemConfigUpdateDto);

    SystemConfigDto addNewSystemConfig(SystemConfigCreateDto systemConfigCreateDto);

    void deleteSystemConfig(Long systemConfigId);

    SystemConfigDto getSystemConfigByNameFromDB(String name);

    SystemConfig getSystemConfigByName(String name);

    List<SystemConfig> getSystemConfigsByName(List<String> configNames);

    Optional<SystemConfig> getSystemConfigByNameOptional(String configName);
}
