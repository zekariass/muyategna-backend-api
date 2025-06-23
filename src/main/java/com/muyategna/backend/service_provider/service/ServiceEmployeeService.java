package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeCreateDto;
import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeDto;
import com.muyategna.backend.service_provider.entity.ServiceEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ServiceEmployeeService {
    Page<ServiceEmployeeDto> getAllServiceEmployeesForServiceProvider(Long serviceProviderId, Pageable pageable);

    ServiceEmployee createServiceEmployee(ServiceEmployee serviceEmployee);

    Boolean isUserAlreadyLinkedToServiceProvider(UUID loggedInUserKeycloakId);

    ServiceEmployeeDto createServiceEmployee(Long serviceProviderId, ServiceEmployeeCreateDto serviceEmployeeDto);

    ServiceEmployeeDto getServiceEmployeeByIdAndServiceProviderId(Long serviceProviderId, Long serviceEmployeeId);

    Boolean activateServiceEmployeeById(Long serviceProviderId, Long serviceEmployeeId);

    Boolean deactivateServiceEmployeeById(Long serviceProviderId, Long serviceEmployeeId);
}
