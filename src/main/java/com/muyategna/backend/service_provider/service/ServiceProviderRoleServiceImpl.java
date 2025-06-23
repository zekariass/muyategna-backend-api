package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceProviderRole;
import com.muyategna.backend.service_provider.repository.ServiceProviderRoleRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceProviderRoleServiceImpl implements ServiceProviderRoleService {

    private final ServiceProviderRoleRepository serviceProviderRoleRepository;

    @Autowired
    public ServiceProviderRoleServiceImpl(ServiceProviderRoleRepository serviceProviderRoleRepository) {
        this.serviceProviderRoleRepository = serviceProviderRoleRepository;
    }


    @Override
    public ServiceProviderRole getServiceProviderRoleByRoleName(String roleName) {
        log.info("Retrieving service provider role by role name: {}", roleName);
        ServiceProviderRole serviceProviderRole = serviceProviderRoleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Service provider role not found with role name: " + roleName));
        log.info("Retrieved service provider role: {}", serviceProviderRole);
        return serviceProviderRole;
    }
}
