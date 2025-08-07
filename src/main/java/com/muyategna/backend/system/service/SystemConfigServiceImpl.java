package com.muyategna.backend.system.service;

import com.muyategna.backend.redis.CommonCacheKeyUtil;
import com.muyategna.backend.system.dto.SystemConfigCreateDto;
import com.muyategna.backend.system.dto.SystemConfigDto;
import com.muyategna.backend.system.dto.SystemConfigUpdateDto;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.mapper.SystemConfigMapper;
import com.muyategna.backend.system.repository.SystemConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;
    private final RedisTemplate<String, Object> systemConfigRedisTemplate;
    //    private final HashOperations <String, String, Object> hashOperations;
    private final static String SYSTEM_CONFIGS_KEY = CommonCacheKeyUtil.systemConfigsKey();

    @Value("${system.config.refresh.interval:1800000}")
    private long refreshInterval; // Default to 30 minutes if not specified in properties

    @Autowired
    public SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository,
                                   RedisTemplate<String, Object> systemConfigRedisTemplate) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigRedisTemplate = systemConfigRedisTemplate;
//        this.hashOperations = systemConfigRedisTemplate.opsForHash();
    }


    @PostConstruct
    public void init() {
        loadAllSystemConfigs();
    }


    /**
     * Scheduled task to refresh the system configuration cache every 30 minutes.
     * The interval can be configured via application properties.
     */
    @Scheduled(fixedRateString = "${system.config.refresh.interval:1800000}") // 30 minutes by default
    public void refreshSystemConfigCache() {
        log.info("Refreshing system configuration cache");
        loadAllSystemConfigs();
    }

    /**
     * Load all system configurations from the database into Redis cache.
     * This method is called during application startup and periodically to ensure the cache is up-to-date.
     */
    private void loadAllSystemConfigs() {
        List<SystemConfig> systemConfigs = systemConfigRepository.findAll(); // Retrieve all system configs

        if (systemConfigs.isEmpty()) {
            log.warn("No system configurations found in the database");
            return;
        }

        Map<String, SystemConfig> tempConfigMap = new HashMap<>();
        systemConfigs.forEach(systemConfig ->
                tempConfigMap.put(systemConfig.getName(), systemConfig));

        systemConfigRedisTemplate.opsForHash().putAll(SYSTEM_CONFIGS_KEY, tempConfigMap);
        systemConfigRedisTemplate.expire(SYSTEM_CONFIGS_KEY, Duration.ofMillis(refreshInterval)); // Add this

        // systemConfigRedisTemplate.opsForHash().putAll(CommonCacheKeyUtil.systemConfigsKey(), tempConfigMap);
        log.info("Loaded/refreshed {} system configurations into memory", systemConfigs.size());
    }


    /**
     * Get a system configuration by its key.
     * If the configuration is not found in the cache, it retrieves it from the database and updates the cache.
     *
     * @param name the key of the system configuration to retrieve
     * @return the system configuration with the specified key
     * @throws ResourceNotFoundException if no system configuration is found with the given key
     */
    public SystemConfig getSystemConfigByName(String name) {
        // Object systemConfig = systemConfigRedisTemplate.opsForHash().get(CommonCacheKeyUtil.systemConfigsKey(), key);
        Object systemConfig = systemConfigRedisTemplate.opsForHash().get(SYSTEM_CONFIGS_KEY, name);

        if (systemConfig == null) {
            log.warn("SystemConfig not found in cache for name: {}", name);
            systemConfig = systemConfigRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("SystemConfig not found with name: " + name));
            systemConfigRedisTemplate.opsForHash().put(SYSTEM_CONFIGS_KEY, name, systemConfig);
        }

        log.info("Retrieved SystemConfig with name: {}", name);
        // Convert the Object back to SystemConfig
        if (!(systemConfig instanceof SystemConfig)) {
            throw new ResourceNotFoundException("SystemConfig not found with name: " + name);
        }
        return (SystemConfig) systemConfig;
    }

    @Override
    public List<SystemConfig> getSystemConfigsByName(List<String> configNames) {
        log.info("Retrieving SystemConfigs with names: {}", configNames);
        return systemConfigRepository.findAllByNameIn(configNames);
    }

    /**
     * Returns a paginated list of all system configurations.
     *
     * @param pageable the pagination information
     * @return a page of SystemConfigDto objects
     */
    @Override
    public Page<SystemConfigDto> getAllSystemConfigsFromDB(Pageable pageable) {
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
    public SystemConfigDto getSystemConfigByIdFromDB(Long systemConfigId) {
        log.info("Retrieving JacksonConfig with ID: {}", systemConfigId);
        SystemConfigDto systemConfigDto = systemConfigRepository
                .findById(systemConfigId)
                .map(SystemConfigMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("JacksonConfig not found with id: " + systemConfigId));
        log.info("Retrieved JacksonConfig with ID: {}", systemConfigId);
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
    public SystemConfigDto getSystemConfigByNameFromDB(String name) {
        log.info("Retrieving JacksonConfig with name: {}", name);
        SystemConfigDto systemConfig = systemConfigRepository
                .findByName(name)
                .map(SystemConfigMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SystemConfig not found with name: " + name));
        log.info("Retrieved JacksonConfig with name: {}", name);
        return systemConfig;
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

        log.info("Updating JacksonConfig with ID: {}", systemConfigId);
        SystemConfig systemConfig = systemConfigRepository.findById(systemConfigId).orElseThrow(() -> new ResourceNotFoundException("JacksonConfig not found with id: " + systemConfigId));

        systemConfig.setName(systemConfigUpdateDto.getName().strip().toLowerCase());
        systemConfig.setValue(systemConfigUpdateDto.getValue());
        systemConfig.setType(systemConfigUpdateDto.getType());

        SystemConfig updatedSystemConfig = systemConfigRepository.save(systemConfig);

        log.info("Updated JacksonConfig with ID: {}", systemConfigId);
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

        log.info("Adding new JacksonConfig with name: {}", systemConfigCreateDto.getName());
        SystemConfig systemConfig = new SystemConfig();

        systemConfig.setName(systemConfigCreateDto.getName().strip().toUpperCase());
        systemConfig.setValue(systemConfigCreateDto.getValue());
        systemConfig.setType(systemConfigCreateDto.getType());

        SystemConfig savedSystemConfig = systemConfigRepository.save(systemConfig);

        log.info("Added new JacksonConfig with ID: {}", savedSystemConfig.getId());
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
        log.info("Deleting JacksonConfig with ID: {}", systemConfigId);
        SystemConfig systemConfig = systemConfigRepository.findById(systemConfigId).orElseThrow(() -> new ResourceNotFoundException("JacksonConfig not found with id: " + systemConfigId));
        systemConfigRepository.delete(systemConfig);
        log.info("Deleted JacksonConfig with ID: {}", systemConfigId);
    }
}
