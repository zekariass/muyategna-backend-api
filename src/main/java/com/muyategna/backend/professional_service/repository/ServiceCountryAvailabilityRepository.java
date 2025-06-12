package com.muyategna.backend.professional_service.repository;

import com.muyategna.backend.professional_service.entity.ServiceCountryAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceCountryAvailabilityRepository extends JpaRepository<ServiceCountryAvailability, Long> {

    Boolean existsByServiceIdAndCountryIdAndIsActiveTrue(Long serviceId, Long countryId);

    List<ServiceCountryAvailability> findByCountryIdAndIsActiveTrue(Long countryId);

    @Query("SELECT sc FROM ServiceCountryAvailability sc " +
            "WHERE sc.country.id = :countryId " +
            "AND sc.isActive = :isActive " +
            "AND LOWER(sc.service.name) LIKE LOWER(CONCAT('%', :serviceName, '%')) ")
    List<ServiceCountryAvailability> findByCountryIdAndIsActiveAndServiceName(@Param("countryId") Long countryId,
                                                                              @Param("isActive") boolean isActive,
                                                                              @Param("serviceName") String serviceName);


    @Query("SELECT sc FROM ServiceCountryAvailability sc " +
            "WHERE sc.country.id = :countryId " +
            "AND sc.isActive = :isActive " +
            "AND sc.service.serviceCategory.id = :categoryId")
    List<ServiceCountryAvailability> findByCountryIdAndIsActiveAndCategoryId(@Param("countryId") Long countryId,
                                                                             @Param("isActive") boolean isActive,
                                                                             @Param("categoryId") Long categoryId);
}

