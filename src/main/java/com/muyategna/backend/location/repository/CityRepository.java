package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByName(String name);

    Optional<City> findByNameAndRegionId(String name, Long regionId);

    @Query("SELECT c FROM City c WHERE c.region.id = :regionId")
    Page<City> findByRegionId(Long regionId, Pageable pageable);

    @Query("SELECT c FROM City c WHERE c.region.id = :regionId")
    List<City> findByRegionId(Long regionId);
}
