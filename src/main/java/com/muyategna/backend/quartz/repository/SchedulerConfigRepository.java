package com.muyategna.backend.quartz.repository;

import com.muyategna.backend.quartz.entity.SchedulerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulerConfigRepository extends JpaRepository<SchedulerConfig, Long> {
    List<SchedulerConfig> findByIsActive(boolean b);
}
