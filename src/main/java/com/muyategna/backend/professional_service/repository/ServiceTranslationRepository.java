package com.muyategna.backend.professional_service.repository;

import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceTranslationRepository extends JpaRepository<ServiceTranslation, Long> {
    Optional<ServiceTranslation> findByServiceIdAndLanguageId(Long serviceId, Long languageId);

    List<ServiceTranslation> findByServiceId(Long serviceId);

    Page<ServiceTranslation> findByServiceId(Long serviceId, Pageable pageable);

    List<ServiceTranslation> findByServiceIdInAndLanguageId(List<Long> pageServiceIds, long id);
}
