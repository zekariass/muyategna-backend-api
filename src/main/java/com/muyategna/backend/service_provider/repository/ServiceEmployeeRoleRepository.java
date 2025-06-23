package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceEmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceEmployeeRoleRepository extends JpaRepository<ServiceEmployeeRole, Long> {

    @Query("SELECT r " +
            "FROM ServiceEmployeeRole r " +
            "WHERE r.employee.userProfile.id = :userProfileId")
    List<ServiceEmployeeRole> findByUserProfileId(
            @Param("userProfileId") Long userProfileId
    );


    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM ServiceEmployeeRole r " +
            "WHERE r.employee.userProfile.id = :userProfileId " +
            "AND r.employee.serviceProvider.id = :providerId " +
            "AND r.role.name IN :roles")
    Boolean existsByUserProfileIdAndServiceProviderIdAndRoleIn(
            @Param("userProfileId") Long userProfileId,
            @Param("providerId") Long providerId,
            @Param("roles") List<String> roles
    );
}
