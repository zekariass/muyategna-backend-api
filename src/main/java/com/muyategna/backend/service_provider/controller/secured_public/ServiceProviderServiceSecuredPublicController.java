package com.muyategna.backend.service_provider.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.service_provider.assembler.ServiceProviderServiceLocalizedDtoModelPublicAssembler;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceService;
import com.muyategna.backend.system.context.CountryContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/secured/public/service-providers/{serviceProviderId}/services")
@Tag(name = "Service Provider Services Endpoints", description = "Endpoints for Service Provider Services")
public class ServiceProviderServiceSecuredPublicController {

    private final ServiceProviderServiceService serviceProviderServiceService;
    private final ServiceProviderServiceLocalizedDtoModelPublicAssembler assembler;

    @Autowired
    public ServiceProviderServiceSecuredPublicController(ServiceProviderServiceService serviceProviderServiceService, ServiceProviderServiceLocalizedDtoModelPublicAssembler assembler) {
        this.serviceProviderServiceService = serviceProviderServiceService;
        this.assembler = assembler;
    }


    @ServiceProviderSecurity(requiredRoles = {"OWNER", "EMPLOYEE", "MANAGER"})
    @GetMapping("/{serviceProviderServiceId}")
    @Operation(summary = "Get Service Provider Service by id", description = "Get a Service Provider Service")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>>> getServiceProviderServiceByIdAndServiceProvider(
            @Parameter(description = "Service Provider Service id") @PathVariable Long serviceProviderServiceId,
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {

        ServiceProviderServiceLocalizedDto serviceProviderService = serviceProviderServiceService.getLocalizedServiceProviderServiceByIdAndServiceProviderId(serviceProviderServiceId, serviceProviderId);
        EntityModel<ServiceProviderServiceLocalizedDto> entityModel = assembler.toModel(serviceProviderService, request);
        ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>> response = ApiResponse.<EntityModel<ServiceProviderServiceLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Service fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @ServiceProviderSecurity(requiredRoles = {"OWNER", "EMPLOYEE", "MANAGER"})
    @GetMapping
    @Operation(summary = "Get Service Provider Services", description = "Get all services offered by a Service Provider")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ServiceProviderServiceLocalizedDto>>>> getServiceProviderServicesForServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {

        List<ServiceProviderServiceLocalizedDto> services = serviceProviderServiceService.getLocalizedServiceProviderServices(serviceProviderId);
        CollectionModel<EntityModel<ServiceProviderServiceLocalizedDto>> entityModels = assembler.toCollectionModel(services, request);
        ApiResponse<CollectionModel<EntityModel<ServiceProviderServiceLocalizedDto>>> response = ApiResponse.<CollectionModel<EntityModel<ServiceProviderServiceLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModels)
                .build();
        return ResponseEntity.ok(response);
    }

    @ServiceProviderSecurity(requiredRoles = {"OWNER", "MANAGER"})
    @PutMapping("/{serviceProviderServiceId}/activate")
    @Operation(summary = "Activate Service Provider Service", description = "Activate a Service Provider Service")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>>> activateServiceProviderService(
            @Parameter(description = "Service Provider Service id") @PathVariable Long serviceProviderServiceId,
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {

        ServiceProviderServiceLocalizedDto serviceProviderService = serviceProviderServiceService.activateServiceProviderService(serviceProviderServiceId, serviceProviderId);
        EntityModel<ServiceProviderServiceLocalizedDto> entityModel = assembler.toModel(serviceProviderService, request);
        ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>> response = ApiResponse.<EntityModel<ServiceProviderServiceLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Service activated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{serviceProviderServiceId}/deactivate")
    @Operation(summary = "Activate Service Provider Service", description = "Activate a Service Provider Service")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>>> inactivateServiceProviderService(
            @Parameter(description = "Service Provider Service id") @PathVariable Long serviceProviderServiceId,
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {

        ServiceProviderServiceLocalizedDto serviceProviderService = serviceProviderServiceService.inactivateServiceProviderService(serviceProviderServiceId, serviceProviderId);
        EntityModel<ServiceProviderServiceLocalizedDto> entityModel = assembler.toModel(serviceProviderService, request);
        ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>> response = ApiResponse.<EntityModel<ServiceProviderServiceLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Service inactivated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(summary = "Create Service Provider Service", description = "Create a Service Provider Service")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>>> createServiceProviderService(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Provider Service object") @Valid @RequestBody ServiceProviderServiceCreateDto serviceProviderServiceDto,
            HttpServletRequest request) {

        Country country = CountryMapper.toEntity(CountryContextHolder.getCountry());
        ServiceProviderServiceLocalizedDto serviceProviderService = serviceProviderServiceService.createServiceProviderService(serviceProviderId, serviceProviderServiceDto, country);
        EntityModel<ServiceProviderServiceLocalizedDto> entityModel = assembler.toModel(serviceProviderService, request);
        ApiResponse<EntityModel<ServiceProviderServiceLocalizedDto>> response = ApiResponse.<EntityModel<ServiceProviderServiceLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Service created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }

}
