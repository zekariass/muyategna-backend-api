package com.muyategna.backend.system.repository;

import com.muyategna.backend.system.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    Optional<SystemConfig> findByName(String name);

    List<SystemConfig> findAllByNameIn(List<String> configNames);
}
