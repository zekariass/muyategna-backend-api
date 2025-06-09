package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.SubCityOrDivisionTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubCityOrDivisionTranslationRepository extends JpaRepository<SubCityOrDivisionTranslation, Long> {
    @Query("SELECT s FROM SubCityOrDivisionTranslation s WHERE s.subCityOrDivision.id = :subCityOrDivisionId AND s.language.id = :languageId")
    Optional<SubCityOrDivisionTranslation> findBySubCityOrDivisionIdAndLanguageId(Long subCityOrDivisionId, long languageId);

    @Query("SELECT s FROM SubCityOrDivisionTranslation s WHERE s.subCityOrDivision.id IN (:subCityOrDivisionIds) AND s.language.id = :languageId")
    List<SubCityOrDivisionTranslation> findBySubCityOrDivisionIdAndLanguageId(List<Long> subCityOrDivisionIds, long languageId);

    @Query("SELECT s FROM SubCityOrDivisionTranslation s WHERE s.subCityOrDivision.id = :subCityOrDivisionId")
    Page<SubCityOrDivisionTranslation> findBySubCityOrDivisionId(Long subCityOrDivisionId, Pageable pageable);
}
