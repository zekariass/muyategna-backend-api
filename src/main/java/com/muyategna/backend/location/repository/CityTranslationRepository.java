package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.CityTranslation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityTranslationRepository extends JpaRepository<CityTranslation, Long> {

    @Query("SELECT c.city.id AS cityId, " +
            "c.name AS name, " +
            "c.description AS description " +
            "FROM CityTranslation c " +
            "WHERE c.city.id IN (:cityIds) AND c.language.id = :languageId")
    List<CityTranslation> findAllByCityIdAndLanguageId(@Param("cityIds") List<Long> cityIds,
                                                       @Param("languageId") Long languageId);

    Optional<CityTranslation> findByCityIdAndLanguageId(Long cityId,
                                                        Long languageId);

    List<CityTranslation> findByLanguageIdAndCityIdIn(long languageId, List<Long> cityIds);

    Page<CityTranslation> findByCityId(Long cityId, Pageable pageable);
}
