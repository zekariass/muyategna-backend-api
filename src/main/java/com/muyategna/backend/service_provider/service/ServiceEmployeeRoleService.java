package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceEmployeeRole;

import java.util.List;

public interface ServiceEmployeeRoleService {
    List<String> getRolesForEmployeeByUserProfileId(Long userProfileId);

    ServiceEmployeeRole createServiceEmployeeRole(ServiceEmployeeRole serviceEmployeeRole);
}
