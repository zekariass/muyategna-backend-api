package com.muyategna.backend.professional_service.repository;

import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceTranslationRepository extends JpaRepository<ServiceTranslation, Long> {
    Optional<ServiceTranslation> findByServiceIdAndLanguageId(Long serviceId, Long languageId);

    List<ServiceTranslation> findByServiceId(Long serviceId);

    Page<ServiceTranslation> findByServiceId(Long serviceId, Pageable pageable);

    @Query("""
            SELECT DISTINCT st FROM ServiceTranslation st
            JOIN FETCH st.service
            WHERE st.service.id IN :pageServiceIds AND st.language.id = :languageId
            """)
    List<ServiceTranslation> findByServiceIdInAndLanguageId(@Param("pageServiceIds") List<Long> pageServiceIds,
                                                            @Param("languageId") long languageId);

    @Query("""
            SELECT DISTINCT st FROM ServiceTranslation st
            JOIN FETCH st.service
            WHERE st.service.id IN :serviceIds AND st.language.id IN :languageIds
            """)
    List<ServiceTranslation> findByServiceIdInAndLanguageIdIn(@Param("serviceIds") List<Long> serviceIds,
                                                              @Param("languageIds") List<Long> languageIds);

}
