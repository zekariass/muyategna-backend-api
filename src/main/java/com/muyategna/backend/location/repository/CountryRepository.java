package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("SELECT c FROM Country c LEFT JOIN FETCH c.regions WHERE c.countryIsoCode2 = :isoCode2")
    Optional<Country> findByCountryIsoCode2WithRegions(@Param("isoCode2") String isoCode2);

    Optional<Country> findByCountryIsoCode2(String isoCode2);

    Optional<Country> findByIsGlobal(boolean b);
}

