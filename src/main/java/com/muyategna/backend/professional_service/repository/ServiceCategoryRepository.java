package com.muyategna.backend.professional_service.repository;

import com.muyategna.backend.professional_service.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    Optional<ServiceCategory> findByName(String name);

    boolean existsByName(String name);
}

