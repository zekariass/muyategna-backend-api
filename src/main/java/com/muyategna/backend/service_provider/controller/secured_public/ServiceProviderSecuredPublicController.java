package com.muyategna.backend.service_provider.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.service_provider.assembler.ServiceProviderDtoModelPublicAssembler;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderUpdateDto;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/secured/public/service-providers")
@Tag(name = "Service Providers Endpoints", description = "Endpoints for Service Providers")
@PreAuthorize("isAuthenticated()")
public class ServiceProviderSecuredPublicController {

    private final ServiceProviderServiceInterface providerServiceInterface;
    private final ServiceProviderDtoModelPublicAssembler assembler;

    @Autowired
    public ServiceProviderSecuredPublicController(ServiceProviderServiceInterface providerServiceInterface,
                                                  ServiceProviderDtoModelPublicAssembler assembler) {
        this.providerServiceInterface = providerServiceInterface;
        this.assembler = assembler;
    }


    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER", "EMPLOYEE"})
    @GetMapping("/{serviceProviderId}")
    @Operation(summary = "Get Service Provider", description = "Get Service Provider")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderDto>>> getServiceProviderById(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {
        ServiceProviderDto serviceProvider = providerServiceInterface.getServiceProviderById(serviceProviderId);
        EntityModel<ServiceProviderDto> entityModel = assembler.toModel(serviceProvider, request);
        ApiResponse<EntityModel<ServiceProviderDto>> response = ApiResponse.<EntityModel<ServiceProviderDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(summary = "Create Service Provider", description = "Create a new Service Provider")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderDto>>> createServiceProvider(
            @Parameter(description = "Service Provider Object") @Valid @RequestBody ServiceProviderCreateDto serviceProviderDto,
            HttpServletRequest request) {

        ServiceProviderDto serviceProvider = providerServiceInterface.createServiceProvider(serviceProviderDto);
        EntityModel<ServiceProviderDto> entityModel = assembler.toModel(serviceProvider, request);
        ApiResponse<EntityModel<ServiceProviderDto>> response = ApiResponse.<EntityModel<ServiceProviderDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Service Provider created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    @PutMapping("/{serviceProviderId}")
    @Operation(summary = "Update Service Provider", description = "Update Service Provider")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderDto>>> updateServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Provider Object") @Valid @RequestBody ServiceProviderUpdateDto serviceProviderDto,
            HttpServletRequest request) {
        ServiceProviderDto serviceProvider = providerServiceInterface.updateServiceProviderById(serviceProviderId, serviceProviderDto);
        EntityModel<ServiceProviderDto> entityModel = assembler.toModel(serviceProvider, request);
        ApiResponse<EntityModel<ServiceProviderDto>> response = ApiResponse.<EntityModel<ServiceProviderDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider updated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    @PutMapping("/{serviceProviderId}/activate")
    @Operation(summary = "Activate Service Provider", description = "Activate Service Provider")
    public ResponseEntity<ApiResponse<EntityModel<Void>>> activateServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {
        Boolean serviceProvider = providerServiceInterface.activateServiceProviderById(serviceProviderId);
//        EntityModel<ServiceProviderDto> entityModel = assembler.toModel(serviceProvider, request);
        ApiResponse<EntityModel<Void>> response = ApiResponse.<EntityModel<Void>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider activated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    @PutMapping("/{serviceProviderId}/deactivate")
    @Operation(summary = "Activate Service Provider", description = "Activate Service Provider")
    public ResponseEntity<ApiResponse<EntityModel<Void>>> inActivateServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {
        Boolean serviceProvider = providerServiceInterface.inActivateServiceProviderById(serviceProviderId);
//        EntityModel<ServiceProviderDto> entityModel = assembler.toModel(serviceProvider, request);
        ApiResponse<EntityModel<Void>> response = ApiResponse.<EntityModel<Void>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider inactivated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
