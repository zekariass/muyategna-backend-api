package com.muyategna.backend.service_provider.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.service_provider.assembler.ServiceProviderAgreementDtoModelPublicAssembler;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementCreateDto;
import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementDto;
import com.muyategna.backend.service_provider.service.ServiceProviderAgreementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/secured/public/service_provider/{serviceProviderId}/agreements")
@Tag(name = "Service Provider Agreements Endpoints", description = "Service Provider Agreements")
@PreAuthorize("isAuthenticated()")
public class ServiceProviderAgreementSecuredPublicController {
    private final ServiceProviderAgreementService serviceProviderAgreementService;
    private final ServiceProviderAgreementDtoModelPublicAssembler assembler;

    @Autowired
    public ServiceProviderAgreementSecuredPublicController(ServiceProviderAgreementService serviceProviderAgreementService, ServiceProviderAgreementDtoModelPublicAssembler assembler) {
        this.serviceProviderAgreementService = serviceProviderAgreementService;
        this.assembler = assembler;
    }


    @ServiceProviderSecurity(requiredRoles = {"OWNER", "MANAGER"})
    @GetMapping
    @Operation(summary = "Get Service Provider Agreements", description = "Get all agreements for a Service Provider")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ServiceProviderAgreementDto>>>> getServiceProviderAgreementsForServiceProvider(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {

        List<ServiceProviderAgreementDto> serviceProviderAgreements = serviceProviderAgreementService.getServiceProviderAgreementsByServiceProviderId(serviceProviderId);
        CollectionModel<EntityModel<ServiceProviderAgreementDto>> serviceProviderAgreementsModel = assembler.toCollectionModel(serviceProviderAgreements, request);

        ApiResponse<CollectionModel<EntityModel<ServiceProviderAgreementDto>>> response = ApiResponse.<CollectionModel<EntityModel<ServiceProviderAgreementDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Agreements fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(serviceProviderAgreementsModel)
                .build();
        return ResponseEntity.ok(response);

    }


    @ServiceProviderSecurity(requiredRoles = {"OWNER", "MANAGER"})
    @GetMapping("/{agreementId}")
    @Operation(summary = "Get Service Provider Agreement", description = "Get Service Provider Agreement")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderAgreementDto>>> getServiceProviderAgreementById(
            @Parameter(description = "Service Provider Agreement id") @PathVariable Long agreementId,
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            HttpServletRequest request) {

        ServiceProviderAgreementDto serviceProviderAgreement = serviceProviderAgreementService.getServiceProviderAgreementByIdAndServiceProviderId(agreementId, serviceProviderId);
        EntityModel<ServiceProviderAgreementDto> serviceProviderAgreementModel = assembler.toModel(serviceProviderAgreement, request);

        ApiResponse<EntityModel<ServiceProviderAgreementDto>> response = ApiResponse.<EntityModel<ServiceProviderAgreementDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Agreement fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(serviceProviderAgreementModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @ServiceProviderSecurity(requiredRoles = {"OWNER", "MANAGER"})
    @PostMapping("/sign")
    @Operation(summary = "Create Service Provider Agreement", description = "Create Service Provider Agreement")
    public ResponseEntity<ApiResponse<EntityModel<ServiceProviderAgreementDto>>> signServiceProviderAgreement(
            @Parameter(description = "Service Provider id") @PathVariable Long serviceProviderId,
            @Parameter(description = "Service Provider Agreement") @RequestBody ServiceProviderAgreementCreateDto serviceProviderAgreementDto,
            HttpServletRequest request) {

        ServiceProviderAgreementDto serviceProviderAgreement = serviceProviderAgreementService.signServiceProviderAgreement(serviceProviderId, serviceProviderAgreementDto);
        EntityModel<ServiceProviderAgreementDto> serviceProviderAgreementModel = assembler.toModel(serviceProviderAgreement, request);

        ApiResponse<EntityModel<ServiceProviderAgreementDto>> response = ApiResponse.<EntityModel<ServiceProviderAgreementDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service Provider Agreement created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(serviceProviderAgreementModel)
                .build();
        return ResponseEntity.ok(response);
    }

}
