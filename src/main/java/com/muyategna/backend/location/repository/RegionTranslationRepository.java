package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.RegionTranslation;
import com.muyategna.backend.location.projection.RegionTranslationView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionTranslationRepository extends JpaRepository<RegionTranslation, Long> {

    @Query("SELECT r.region.id AS regionId, " +
            "r.name AS name, " +
            "r.description AS description " +
            "FROM RegionTranslation r " +
            "WHERE r.region.id IN (:regionIds) AND r.language.id = :languageId")
    List<RegionTranslationView> findByRegionIdsAndLanguageId(@Param("regionIds") List<Long> regionIds,
                                                             @Param("languageId") Long languageId);

    @Query("SELECT r " +
            "FROM RegionTranslation r " +
            "WHERE r.region.id = :regionId")
    List<RegionTranslation> findByRegionId(@Param("regionId") Long regionId);

    @Query("SELECT r " +
            "FROM RegionTranslation r " +
            "WHERE r.region.id = :regionId AND r.language.id = :languageId")
    Optional<RegionTranslation> findByRegionIdAndLanguageId(@Param("regionId") Long regionId,
                                                            @Param("languageId") Long languageId);


    @Query("SELECT r " +
            "FROM RegionTranslation r " +
            "WHERE r.region.id IN :regionIds AND r.language.id = :languageId")
    List<RegionTranslation> findAllByRegionIdsAndLanguageId(@Param("regionIds") List<Long> regionIds,
                                                            @Param("languageId") Long languageId);


    @Query("SELECT r " +
            "FROM RegionTranslation r " +
            "WHERE r.region.id = :regionId AND r.language.locale = :languageLocale")
    Optional<RegionTranslation> findByRegionIdAndLocale(@Param("regionId") Long regionId,
                                                        @Param("languageLocale") String languageLocale);
}
