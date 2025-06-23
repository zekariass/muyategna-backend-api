package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeCreateDto;
import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeDto;
import com.muyategna.backend.service_provider.entity.ServiceEmployee;
import com.muyategna.backend.service_provider.entity.ServiceEmployeeRole;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderRole;
import com.muyategna.backend.service_provider.mapper.ServiceEmployeeMapper;
import com.muyategna.backend.service_provider.mapper.ServiceEmployeeRoleMapper;
import com.muyategna.backend.service_provider.repository.ServiceEmployeeRepository;
import com.muyategna.backend.service_provider.repository.ServiceEmployeeRoleRepository;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.service_provider.repository.ServiceProviderRoleRepository;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.repository.UserProfileRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class ServiceEmployeeServiceImpl implements ServiceEmployeeService {

    private final ServiceEmployeeRepository serviceEmployeeRepository;
    private final UserProfileRepository userProfileRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceEmployeeRoleRepository serviceEmployeeRoleRepository;
    private final ServiceProviderRoleRepository serviceProviderRoleRepository;

    @Autowired
    public ServiceEmployeeServiceImpl(ServiceEmployeeRepository serviceEmployeeRepository, UserProfileRepository userProfileRepository, ServiceProviderRepository serviceProviderRepository, ServiceEmployeeRoleRepository serviceEmployeeRoleRepository, ServiceProviderRoleRepository serviceProviderRoleRepository) {
        this.serviceEmployeeRepository = serviceEmployeeRepository;
        this.userProfileRepository = userProfileRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceEmployeeRoleRepository = serviceEmployeeRoleRepository;
        this.serviceProviderRoleRepository = serviceProviderRoleRepository;
    }

    @Override
    public Page<ServiceEmployeeDto> getAllServiceEmployeesForServiceProvider(Long serviceProviderId, Pageable pageable) {

        log.info("Retrieving all service employees for service provider with ID: {}", serviceProviderId);
        Page<ServiceEmployeeDto> serviceEmployees = serviceEmployeeRepository.findAllByServiceProviderId(serviceProviderId, pageable)
                .map(ServiceEmployeeMapper::toDto);
        log.info("Retrieved {} service employees for service provider with ID: {}", serviceEmployees.getTotalElements(), serviceProviderId);
        return serviceEmployees;
    }

    @Override
    public ServiceEmployee createServiceEmployee(ServiceEmployee serviceEmployee) {
        log.info("Creating service employee: {}", serviceEmployee);
        ServiceEmployee savedServiceEmployee = serviceEmployeeRepository.save(serviceEmployee);
        log.info("Created service employee with ID: {}", savedServiceEmployee.getId());
        return savedServiceEmployee;
    }

    @Override
    public Boolean isUserAlreadyLinkedToServiceProvider(UUID loggedInUserKeycloakId) {
        log.info("Checking if user with ID: {} is already linked to a service provider", loggedInUserKeycloakId);
        return serviceEmployeeRepository.existsByUserKeycloakId(loggedInUserKeycloakId);
    }

    @Transactional
    @Override
    public ServiceEmployeeDto createServiceEmployee(Long serviceProviderId, ServiceEmployeeCreateDto serviceEmployeeDto) {
        log.info("Creating service employee from service employee dto");

        // Find the logged-in user's profile
        UserProfile userProfile = userProfileRepository.findById(serviceEmployeeDto.getUserProfileId()).orElseThrow(() -> new RuntimeException("User profile not found with ID: " + serviceEmployeeDto.getUserProfileId()));

        // Check if user is already linked to a service provider
        if (serviceEmployeeRepository.existsByUserProfileId(serviceEmployeeDto.getUserProfileId())) {
            throw new BadRequestException("User is already linked to a service provider");
        }
        // Find the service provider
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(() -> new RuntimeException("Service provider not found"));

        // Map the service employee dto to an entity
        ServiceEmployee serviceEmployee = ServiceEmployeeMapper.toEntity(serviceEmployeeDto, serviceProvider, userProfile);

        // Save the service employee
        serviceEmployee = serviceEmployeeRepository.save(serviceEmployee);

        log.info("Created service employee with ID : {}", serviceEmployee.getId());

        // Assign role to the created service employee
        assignRoleToServiceEmployee(serviceEmployeeDto, serviceEmployee);

        return ServiceEmployeeMapper.toDto(serviceEmployee);
    }


    private void assignRoleToServiceEmployee(ServiceEmployeeCreateDto serviceEmployeeDto, ServiceEmployee serviceEmployee) {
        Long serviceProviderRoleId = serviceEmployeeDto.getServiceEmployeeRole().getServiceProviderRoleId();
        ServiceProviderRole serviceProviderRole = serviceProviderRoleRepository.findById(serviceProviderRoleId).orElseThrow(() -> new RuntimeException("Service Provider role not found with ID: " + serviceProviderRoleId));
        ServiceEmployeeRole serviceEmployeeRole = ServiceEmployeeRoleMapper.toEntity(serviceEmployeeDto.getServiceEmployeeRole(), serviceProviderRole, serviceEmployee);
        serviceEmployeeRoleRepository.save(serviceEmployeeRole);
    }

    @Override
    public ServiceEmployeeDto getServiceEmployeeByIdAndServiceProviderId(Long serviceProviderId, Long serviceEmployeeId) {
        log.info("Retrieving service employee with ID: {} for service provider with ID: {}", serviceEmployeeId, serviceProviderId);
        ServiceEmployee serviceEmployee = serviceEmployeeRepository.findByIdAndServiceProviderId(serviceEmployeeId, serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service employee not found"));
        log.info("Retrieved service employee with ID: {}", serviceEmployee.getId());
        return ServiceEmployeeMapper.toDto(serviceEmployee);
    }

    @Transactional
    @Override
    public Boolean activateServiceEmployeeById(Long serviceProviderId, Long serviceEmployeeId) {
        log.info("Activating service employee with ID: {} for service provider with ID: {}", serviceEmployeeId, serviceProviderId);
        Integer rowsAffected = serviceEmployeeRepository.updateIsActiveByIdAndServiceProviderId(true, serviceEmployeeId, serviceProviderId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Service employee not found");
        }
        log.info("Activated service employee with ID: {}", serviceEmployeeId);
        return true;
    }

    @Transactional
    @Override
    public Boolean deactivateServiceEmployeeById(Long serviceProviderId, Long serviceEmployeeId) {
        log.info("Deactivating service employee with ID: {} for service provider with ID: {}", serviceEmployeeId, serviceProviderId);
        Integer rowsAffected = serviceEmployeeRepository.updateIsActiveByIdAndServiceProviderId(false, serviceEmployeeId, serviceProviderId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Service employee not found");
        }
        log.info("Deactivated service employee with ID: {}", serviceEmployeeId);
        return true;
    }
}
