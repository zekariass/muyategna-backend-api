package com.muyategna.backend.location.repository;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.CountryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryTranslationRepository extends JpaRepository<CountryTranslation, Long> {

    @Query("SELECT c.country.id AS countryId, " +
            "c.name AS name, " +
            "c.continent AS continent, " +
            "c.description AS description " +
            "FROM CountryTranslation c " +
            "WHERE c.country.id IN (:countryIds) AND c.language.id = :languageId")
    List<CountryTranslation> findAllByCountryIdAndLanguageId(@Param("countryIds") List<Long> countryIds,
                                                             @Param("languageId") Long languageId);

    @Query("SELECT c FROM CountryTranslation c WHERE c.country.id = :countryId AND c.language.id = :languageId")
    Optional<CountryTranslation> findByCountryIdAndLanguageId(@Param("countryId") Long countryId,
                                                              @Param("languageId") Long languageId);


    @Query("SELECT c FROM CountryTranslation c WHERE c.country.id = :countryId AND c.language.id = :languageId")
    Optional<CountryTranslation> findAllByCountryIdAndLanguageId(@Param("countryId") Long countryId,
                                                                 @Param("languageId") Long languageId);

    @Query("SELECT c FROM CountryTranslation c WHERE c.country.id = :countryId AND c.language.locale = :languageLocale")
    Optional<CountryTranslation> findByCountryIdAndLanguageCode(@Param("countryId") Long countryId,
                                                                @Param("languageLocale") String languageLocale);

    @Query("SELECT c FROM CountryTranslation c WHERE c.country.countryIsoCode2 = :countryIsoCode2 AND c.language.locale = :languageLocale")
    Optional<CountryTranslation> findByCountryCodeIso2AndLanguageCode(@Param("countryIsoCode2") String countryIsoCode2,
                                                                      @Param("languageLocale") String languageLocale);

    @Query("SELECT c FROM CountryTranslation c WHERE c.country.id = :countryId")
    List<CountryTranslation> findByCountryId(@Param("countryId") Long countryId);

    Optional<CountryTranslation> findByCountryAndLanguage(Country country, Language language);
}
