package com.muyategna.backend.service_provider.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.PublicPaginationService;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.service_provider.assembler.ServiceEmployeeDtoModelPublicAssembler;
import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeCreateDto;
import com.muyategna.backend.service_provider.dto.service_employee.ServiceEmployeeDto;
import com.muyategna.backend.service_provider.entity.ServiceEmployee;
import com.muyategna.backend.service_provider.service.ServiceEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/secured/public/service-providers/{serviceProviderId}/employees")
@Tag(name = "Service Employee Endpoints", description = "Endpoints for Service Employees")
@PreAuthorize("isAuthenticated()")
public class ServiceEmployeeSecuredPublicController {
    private final ServiceEmployeeService serviceEmployeeService;
    private final ServiceEmployeeDtoModelPublicAssembler assembler;
    private final PagedResourcesAssembler<ServiceEmployeeDto> pagedResourcesAssembler;
    private final PublicPaginationService publicPaginationService;

    @Autowired
    public ServiceEmployeeSecuredPublicController(ServiceEmployeeService serviceEmployeeService, ServiceEmployeeDtoModelPublicAssembler assembler, PagedResourcesAssembler<ServiceEmployeeDto> pagedResourcesAssembler, PublicPaginationService publicPaginationService) {
        this.serviceEmployeeService = serviceEmployeeService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.publicPaginationService = publicPaginationService;
    }


    @GetMapping
    @Operation(summary = "Get all Service Employees", description = "Get all Service Employees")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER", "EMPLOYEE"})
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ServiceEmployeeDto>>>> getAllServiceEmployeesForServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort by") @RequestParam(defaultValue = "id,asc") String sort,
            HttpServletRequest request
    ) {

        Pageable pageable = publicPaginationService.getPagination(page, size, sort, ServiceEmployee.class);
        Page<ServiceEmployeeDto> serviceEmployees = serviceEmployeeService.getAllServiceEmployeesForServiceProvider(serviceProviderId, pageable);
        PagedModel<EntityModel<ServiceEmployeeDto>> pagedModel = pagedResourcesAssembler.toModel(serviceEmployees,
                employeeDto -> assembler.toModel(employeeDto, request)
        );

        ApiResponse<PagedModel<EntityModel<ServiceEmployeeDto>>> response = ApiResponse.<PagedModel<EntityModel<ServiceEmployeeDto>>>builder()
                .statusCode(200)
                .success(true)
                .message("Service Employees fetched successfully")
                .path(request.getRequestURI())
                .timestamp(java.time.LocalDateTime.now())
                .data(pagedModel)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{serviceEmployeeId}")
    @Operation(summary = "Get Service Employee", description = "Get Service Employee")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER", "EMPLOYEE"})
    public ResponseEntity<ApiResponse<EntityModel<ServiceEmployeeDto>>> getServiceEmployeeById(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Employee id") @PathVariable Long serviceEmployeeId,
            HttpServletRequest request) {
        ServiceEmployeeDto serviceEmployee = serviceEmployeeService.getServiceEmployeeByIdAndServiceProviderId(serviceProviderId, serviceEmployeeId);
        EntityModel<ServiceEmployeeDto> entityModel = assembler.toModel(serviceEmployee, request);
        ApiResponse<EntityModel<ServiceEmployeeDto>> response = ApiResponse.<EntityModel<ServiceEmployeeDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Employee fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create Service Employee", description = "Create Service Employee")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<EntityModel<ServiceEmployeeDto>>> createServiceEmployee(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Employee") @Valid @RequestBody ServiceEmployeeCreateDto serviceEmployeeDto,
            HttpServletRequest request) {
        ServiceEmployeeDto serviceEmployee = serviceEmployeeService.createServiceEmployee(serviceProviderId, serviceEmployeeDto);
        EntityModel<ServiceEmployeeDto> entityModel = assembler.toModel(serviceEmployee, request);
        ApiResponse<EntityModel<ServiceEmployeeDto>> response = ApiResponse.<EntityModel<ServiceEmployeeDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Employee created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{serviceEmployeeId}/activate")
    @Operation(summary = "Activate Service Employee", description = "Activate Service Employee")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<EntityModel<Void>>> activateServiceEmployee(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Employee id") @PathVariable Long serviceEmployeeId,
            HttpServletRequest request) {
        Boolean serviceEmployee = serviceEmployeeService.activateServiceEmployeeById(serviceProviderId, serviceEmployeeId);
//        EntityModel<ServiceEmployeeDto> entityModel = assembler.toModel(serviceEmployee, request);
        ApiResponse<EntityModel<Void>> response = ApiResponse.<EntityModel<Void>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Employee activated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{serviceEmployeeId}/deactivate")
    @Operation(summary = "Deactivate Service Employee", description = "Deactivate Service Employee")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<EntityModel<Void>>> deactivateServiceEmployee(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Employee id") @PathVariable Long serviceEmployeeId,
            HttpServletRequest request) {
        Boolean serviceEmployee = serviceEmployeeService.deactivateServiceEmployeeById(serviceProviderId, serviceEmployeeId);
//        EntityModel<ServiceEmployeeDto> entityModel = assembler.toModel(serviceEmployee, request);
        ApiResponse<EntityModel<Void>> response = ApiResponse.<EntityModel<Void>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Employee deactivated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
