package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByName(String name);

    Optional<Region> findByNameAndCountryId(String name, Long countryId);

//    @Query("SELECT r FROM Region r LEFT JOIN FETCH r.cities WHERE r.country = :country")
//    List<Region> findByCountryWithCities(Country country);

    List<Region> findByCountry(Country country);

    Optional<Region> findByIdAndCountry(Long regionId, Country country);

    @Query("SELECT r FROM Region r WHERE r.country.id = :countryId")
    List<Region> findByCountryId(Long countryId);

    @Query("SELECT r FROM Region r WHERE r.country.id = :countryId")
    Page<Region> findByCountryId(Long countryId, Pageable pageable);

}
