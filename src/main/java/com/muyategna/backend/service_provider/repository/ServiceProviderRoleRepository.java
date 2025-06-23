package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProviderRoleRepository extends JpaRepository<ServiceProviderRole, Long> {
    Optional<ServiceProviderRole> findByName(String roleName);
}
