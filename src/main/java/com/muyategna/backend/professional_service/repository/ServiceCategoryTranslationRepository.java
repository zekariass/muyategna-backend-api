package com.muyategna.backend.professional_service.repository;

import com.muyategna.backend.professional_service.entity.ServiceCategoryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryTranslationRepository extends JpaRepository<ServiceCategoryTranslation, Long> {

    Optional<ServiceCategoryTranslation> findByServiceCategoryName(String name);

    Optional<ServiceCategoryTranslation> findByServiceCategoryNameAndLanguageId(String serviceCategoryName, Long languageId);

    Optional<ServiceCategoryTranslation> findByServiceCategoryIdAndLanguageId(Long serviceCategoryId, Long languageId);

    boolean existsByName(String name);

    List<ServiceCategoryTranslation> findByServiceCategoryIdInAndLanguageId(List<Long> categoryIds, long languageId);
}

