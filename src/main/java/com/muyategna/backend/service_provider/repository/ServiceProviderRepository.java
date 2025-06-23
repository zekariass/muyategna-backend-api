package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    @Modifying
    @Query("UPDATE ServiceProvider s " +
            "SET s.isActive = true, " +
            "s.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE s.id = :serviceProviderId")
    Integer activateServiceProviderById(Long serviceProviderId);


    @Modifying
    @Query("UPDATE ServiceProvider s " +
            "SET s.isActive = false, " +
            "s.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE s.id = :serviceProviderId")
    Integer inActivateServiceProviderById(Long serviceProviderId);
}
