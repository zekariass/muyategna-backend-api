package com.muyategna.backend.common.repository;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.location.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByCountryAndIsDefault(Country country, boolean isDefault);

    Optional<Language> findByLocale(String locale);

    List<Language> findByCountry(Country country);

    Optional<Language> findByIsGlobal(boolean b);

    List<Language> findByCountryId(Long countryId);
}
