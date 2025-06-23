package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceEmployeeRole;
import com.muyategna.backend.service_provider.repository.ServiceEmployeeRepository;
import com.muyategna.backend.service_provider.repository.ServiceEmployeeRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServiceEmployeeRoleServiceImpl implements ServiceEmployeeRoleService {

    private final ServiceEmployeeRoleRepository serviceEmployeeRoleRepository;
    private final ServiceEmployeeRepository serviceEmployeeRepository;

    @Autowired
    public ServiceEmployeeRoleServiceImpl(ServiceEmployeeRoleRepository serviceEmployeeRoleRepository, ServiceEmployeeRepository serviceEmployeeRepository) {
        this.serviceEmployeeRoleRepository = serviceEmployeeRoleRepository;
        this.serviceEmployeeRepository = serviceEmployeeRepository;
    }

    @Override
    public List<String> getRolesForEmployeeByUserProfileId(Long userProfileId) {
//        // Find the service employee by user profile ID
//        ServiceEmployee serviceEmployee = serviceEmployeeRepository.findByUserProfileId(userProfileId)
//                .orElseThrow(() -> new ResourceNotFoundException("Service employee not found for user profile ID: " + userProfileId));

        // Fetch roles associated with the service employee
        List<ServiceEmployeeRole> roles = serviceEmployeeRoleRepository.findByUserProfileId(userProfileId);

        // If no roles are found, return an empty list
        return roles.stream()
                .map(role -> role.getRole().getName())
                .toList();
    }

    @Override
    public ServiceEmployeeRole createServiceEmployeeRole(ServiceEmployeeRole serviceEmployeeRole) {
        log.info("Creating service employee role");
        ServiceEmployeeRole savedServiceEmployeeRole = serviceEmployeeRoleRepository.save(serviceEmployeeRole);
        log.info("Created service employee role with ID: {}", savedServiceEmployeeRole.getId());
        return savedServiceEmployeeRole;
    }
}
