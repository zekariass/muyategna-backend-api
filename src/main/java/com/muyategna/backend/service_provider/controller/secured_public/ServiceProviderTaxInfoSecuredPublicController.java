package com.muyategna.backend.service_provider.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.service_provider.assembler.ServiceProviderTaxInfoDtoModelPublicAssembler;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoUpdateDto;
import com.muyategna.backend.service_provider.service.ServiceProviderTaxInfoService;
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
@RequestMapping("/api/v1/secured/public/service-providers/{serviceProviderId}/tax-infos")
@Tag(name = "Service Provider Tax Info Endpoints", description = "Endpoints for Service Provider Tax Info")
@PreAuthorize("isAuthenticated()")
public class ServiceProviderTaxInfoSecuredPublicController {

    private final ServiceProviderTaxInfoService serviceProviderTaxInfoService;
    private final ServiceProviderTaxInfoDtoModelPublicAssembler assembler;

    @Autowired
    public ServiceProviderTaxInfoSecuredPublicController(ServiceProviderTaxInfoService serviceProviderTaxInfoService, ServiceProviderTaxInfoDtoModelPublicAssembler assembler) {
        this.serviceProviderTaxInfoService = serviceProviderTaxInfoService;
        this.assembler = assembler;
    }

    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    @GetMapping("/{serviceProviderTaxInfoId}")
    @Operation(summary = "Get Service Provider Tax Info", description = "Get Service Provider Tax Info")
    public ResponseEntity<ApiResponse<ServiceProviderTaxInfoDto>> getServiceProviderTaxInfoByIdAndServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Provider Tax Info id") @PathVariable Long serviceProviderTaxInfoId,
            HttpServletRequest request) {
        ServiceProviderTaxInfoDto serviceProviderTaxInfo = serviceProviderTaxInfoService.getServiceProviderTaxInfoAndServiceProviderId(serviceProviderTaxInfoId, serviceProviderId);
        ApiResponse<ServiceProviderTaxInfoDto> response = ApiResponse.<ServiceProviderTaxInfoDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Tax Info fetched successfully")
                .timestamp(LocalDateTime.now())
                .data(serviceProviderTaxInfo)
                .build();
        return ResponseEntity.ok(response);
    }


    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    @PutMapping("/{serviceProviderTaxInfoId}")
    @Operation(summary = "Update Service Provider Tax Info", description = "Update Service Provider Tax Info")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderTaxInfoDto>>> updateServiceProviderTaxInfo(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Provider Tax Info id") @PathVariable Long serviceProviderTaxInfoId,
            @Parameter(description = "Service Provider Tax Info Object") @Valid @RequestBody ServiceProviderTaxInfoUpdateDto serviceProviderTaxInfoDto,
            HttpServletRequest request) {
        ServiceProviderTaxInfoDto serviceProviderTaxInfo = serviceProviderTaxInfoService.updateServiceProviderTaxInfo(serviceProviderId, serviceProviderTaxInfoId, serviceProviderTaxInfoDto);
        EntityModel<ServiceProviderTaxInfoDto> entityModel = assembler.toModel(serviceProviderTaxInfo, request);
        ApiResponse<EntityModel<ServiceProviderTaxInfoDto>> response = ApiResponse.<EntityModel<ServiceProviderTaxInfoDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Tax Info updated successfully")
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }
}
