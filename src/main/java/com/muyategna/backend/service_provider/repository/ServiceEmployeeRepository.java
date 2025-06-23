package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ServiceEmployeeRepository extends JpaRepository<ServiceEmployee, Long> {
    Optional<ServiceEmployee> findByUserProfileId(Long userProfileId);

    Page<ServiceEmployee> findAllByServiceProviderId(Long serviceProviderId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(se) > 0 THEN true ELSE false END " +
            "FROM ServiceEmployee se " +
            "WHERE se.userProfile.keycloakUserId = :loggedInUserKeycloakId")
    Boolean existsByUserKeycloakId(@Param("loggedInUserKeycloakId") UUID loggedInUserKeycloakId);

    Optional<ServiceEmployee> findByIdAndServiceProviderId(Long serviceEmployeeId, Long serviceProviderId);

    @Modifying
    @Query("UPDATE ServiceEmployee se " +
            "SET se.isActive = :isActive " +
            "WHERE se.id = :serviceEmployeeId " +
            "AND se.serviceProvider.id = :serviceProviderId")
    Integer updateIsActiveByIdAndServiceProviderId(
            @Param("isActive") boolean isActive,
            @Param("serviceEmployeeId") Long serviceEmployeeId,
            @Param("serviceProviderId") Long serviceProviderId
    );

    @Query("SELECT CASE WHEN COUNT(se) > 0 THEN true ELSE false END " +
            "FROM ServiceEmployee se " +
            "WHERE se.userProfile.id = :userProfileId")
    boolean existsByUserProfileId(@Param("userProfileId") Long userProfileId);
}
