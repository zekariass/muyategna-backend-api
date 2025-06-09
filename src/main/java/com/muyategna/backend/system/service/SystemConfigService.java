package com.muyategna.backend.system.service;

import com.muyategna.backend.system.dto.SystemConfigCreateDto;
import com.muyategna.backend.system.dto.SystemConfigDto;
import com.muyategna.backend.system.dto.SystemConfigUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SystemConfigService {
    Page<SystemConfigDto> getAllSystemConfigs(Pageable pageable);

    SystemConfigDto getSystemConfigById(Long systemConfigId);

    SystemConfigDto getSystemConfigByName(String name);

    SystemConfigDto updateSystemConfig(Long systemConfigId, SystemConfigUpdateDto systemConfigUpdateDto);

    SystemConfigDto addNewSystemConfig(SystemConfigCreateDto systemConfigCreateDto);

    void deleteSystemConfig(Long systemConfigId);
}
