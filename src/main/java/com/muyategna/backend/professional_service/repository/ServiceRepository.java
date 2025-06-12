package com.muyategna.backend.professional_service.repository;

import com.muyategna.backend.professional_service.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(String name);

    Page<Service> findByServiceCategoryId(Long serviceCategoryId, Pageable pageable);

    List<Service> findByServiceCategoryId(Long serviceCategoryId);

    Page<Service> findByIdIn(List<Long> serviceIds, Pageable pageable);

    List<Service> findByIdIn(List<Long> serviceIds);

}
