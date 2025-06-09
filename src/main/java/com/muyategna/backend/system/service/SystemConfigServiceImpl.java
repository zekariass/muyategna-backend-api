package com.muyategna.backend.system.service;

import com.muyategna.backend.system.dto.SystemConfigCreateDto;
import com.muyategna.backend.system.dto.SystemConfigDto;
import com.muyategna.backend.system.dto.SystemConfigUpdateDto;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.mapper.SystemConfigMapper;
import com.muyategna.backend.system.repository.SystemConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Autowired
    public SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }


    /**
     * Returns a paginated list of all system configurations.
     *
     * @param pageable the pagination information
     * @return a page of SystemConfigDto objects
     */
    @Override
    public Page<SystemConfigDto> getAllSystemConfigs(Pageable pageable) {
        log.info("Retrieving all SystemConfigs with pagination");
        Page<SystemConfigDto> systemConfigDtoList = systemConfigRepository
                .findAll(pageable)
                .map(SystemConfigMapper::toDto);
        log.info("Retrieved {} SystemConfigs", systemConfigDtoList.getTotalElements());
        return systemConfigDtoList;
    }


    /**
     * Get a system configuration by its ID.
     *
     * @param systemConfigId the ID of the system configuration to retrieve
     * @return the system configuration with the specified ID
     * @throws ResourceNotFoundException if no system configuration is found with the given ID
     */
    @Override
    public SystemConfigDto getSystemConfigById(Long systemConfigId) {
        log.info("Retrieving SystemConfig with ID: {}", systemConfigId);
        SystemConfigDto systemConfigDto = systemConfigRepository
                .findById(systemConfigId)
                .map(SystemConfigMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SystemConfig not found with id: " + systemConfigId));
        log.info("Retrieved SystemConfig with ID: {}", systemConfigId);
        return systemConfigDto;
    }

    /**
     * Get a system configuration by its name.
     *
     * @param name the name of the system configuration to retrieve
     * @return the system configuration with the specified name
     * @throws ResourceNotFoundException if no system configuration is found with the given name
     */
    @Override
    public SystemConfigDto getSystemConfigByName(String name) {
        log.info("Retrieving SystemConfig with name: {}", name);
        SystemConfigDto systemConfigDto = systemConfigRepository
                .findByName(name)
                .map(SystemConfigMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SystemConfig not found with name: " + name));
        log.info("Retrieved SystemConfig with name: {}", name);
        return systemConfigDto;
    }

    /**
     * Update an existing system configuration.
     *
     * @param systemConfigId        the ID of the system configuration to update
     * @param systemConfigUpdateDto the updated system configuration data
     * @return the updated system configuration
     * @throws ResourceNotFoundException if no system configuration is found with the given ID
     */
    @Override
    public SystemConfigDto updateSystemConfig(Long systemConfigId, SystemConfigUpdateDto systemConfigUpdateDto) {

        log.info("Updating SystemConfig with ID: {}", systemConfigId);
        SystemConfig systemConfig = systemConfigRepository.findById(systemConfigId).orElseThrow(() -> new ResourceNotFoundException("SystemConfig not found with id: " + systemConfigId));

        systemConfig.setName(systemConfigUpdateDto.getName().strip().toLowerCase());
        systemConfig.setValue(systemConfigUpdateDto.getValue());
        systemConfig.setType(systemConfigUpdateDto.getType());

        SystemConfig updatedSystemConfig = systemConfigRepository.save(systemConfig);

        log.info("Updated SystemConfig with ID: {}", systemConfigId);
        return SystemConfigMapper.toDto(updatedSystemConfig);
    }

    /**
     * Add a new system configuration.
     *
     * @param systemConfigCreateDto the system configuration data to create
     * @return the created system configuration
     */
    @Override
    public SystemConfigDto addNewSystemConfig(SystemConfigCreateDto systemConfigCreateDto) {

        log.info("Adding new SystemConfig with name: {}", systemConfigCreateDto.getName());
        SystemConfig systemConfig = new SystemConfig();

        systemConfig.setName(systemConfigCreateDto.getName().strip().toUpperCase());
        systemConfig.setValue(systemConfigCreateDto.getValue());
        systemConfig.setType(systemConfigCreateDto.getType());

        SystemConfig savedSystemConfig = systemConfigRepository.save(systemConfig);

        log.info("Added new SystemConfig with ID: {}", savedSystemConfig.getId());
        return SystemConfigMapper.toDto(savedSystemConfig);
    }

    
    /**
     * Delete a system configuration by its ID.
     *
     * @param systemConfigId the ID of the system configuration to delete
     * @throws ResourceNotFoundException if no system configuration is found with the given ID
     */
    @Override
    public void deleteSystemConfig(Long systemConfigId) {
        log.info("Deleting SystemConfig with ID: {}", systemConfigId);
        SystemConfig systemConfig = systemConfigRepository.findById(systemConfigId).orElseThrow(() -> new ResourceNotFoundException("SystemConfig not found with id: " + systemConfigId));
        systemConfigRepository.delete(systemConfig);
        log.info("Deleted SystemConfig with ID: {}", systemConfigId);
    }
}
