package com.muyategna.backend.location.repository;

import com.muyategna.backend.location.entity.SubCityOrDivision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCityOrDivisionRepository extends JpaRepository<SubCityOrDivision, Long> {

    List<SubCityOrDivision> findByCityId(Long cityId);

    Page<SubCityOrDivision> findByCityId(Long cityId, Pageable pageable);
}
