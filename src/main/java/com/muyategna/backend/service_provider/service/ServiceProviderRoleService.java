package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceProviderRole;


public interface ServiceProviderRoleService {
    ServiceProviderRole getServiceProviderRoleByRoleName(String roleName);
}
