package com.muyategna.backend.job_request.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.PublicPaginationService;
import com.muyategna.backend.job_request.assembler.JobLeadClaimDetailCustomerViewDtoModelAssembler;
import com.muyategna.backend.job_request.assembler.JobLeadClaimSPDetailDtoModelAssembler;
import com.muyategna.backend.job_request.assembler.JobLeadClaimSPListDtoModelAssembler;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimCreateDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDetailCustomerViewDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDetailSPViewDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimSPListViewDto;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.job_request.service.JobLeadClaimService;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/secured/public/job-lead-claim")
@Tag(name = "Job Lead Claim", description = "Job Lead Claim Secured Public API")
public class JobLeadClaimSecuredPublicController {

    private final JobLeadClaimService jobLeadClaimService;
    private final PagedResourcesAssembler<JobLeadClaimSPListViewDto> pagedSPListAssembler;
    private final JobLeadClaimSPListDtoModelAssembler spListDtoModelAssembler;
    private final JobLeadClaimSPDetailDtoModelAssembler spDetailDtoModelAssembler;
    private final JobLeadClaimDetailCustomerViewDtoModelAssembler detailCustomerViewDtoModelAssembler;
    private final PublicPaginationService paginationService;

    @Autowired
    public JobLeadClaimSecuredPublicController(JobLeadClaimService jobLeadClaimService, PagedResourcesAssembler<JobLeadClaimSPListViewDto> pagedAssembler, JobLeadClaimSPListDtoModelAssembler spListDtoModelAssembler, JobLeadClaimSPDetailDtoModelAssembler spDetailDtoModelAssembler, JobLeadClaimDetailCustomerViewDtoModelAssembler detailCustomerViewDtoModelAssembler, PublicPaginationService paginationService) {
        this.jobLeadClaimService = jobLeadClaimService;
        this.pagedSPListAssembler = pagedAssembler;
        this.spListDtoModelAssembler = spListDtoModelAssembler;
        this.spDetailDtoModelAssembler = spDetailDtoModelAssembler;
        this.detailCustomerViewDtoModelAssembler = detailCustomerViewDtoModelAssembler;
        this.paginationService = paginationService;
    }

    @PostMapping
    @Operation(summary = "Claim a job lead", description = "Claim a job lead by service provider")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<Void>> claimJobLead(
            @Parameter(description = "Service Provider ID", required = true) @RequestParam Long serviceProviderId,
            @Parameter(description = "Job quote object", required = true) @Valid @RequestBody JobLeadClaimCreateDto jobLeadClaimCreateDto,
            HttpServletRequest request
    ) {
        if (!Objects.equals(serviceProviderId, jobLeadClaimCreateDto.getServiceProviderId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<Void>builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .success(false)
                    .message("Service Provider ID in URL and job lead claim object do not match")
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        jobLeadClaimService.claimJobLead(jobLeadClaimCreateDto);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job lead claimed successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping
    @Operation(summary = "Get all job leads by status for service provider", description = "Get all job leads by status for service provider")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<JobLeadClaimSPListViewDto>>>> getClaimsForServiceProviderByStatus(
            @Parameter(description = "Service Provider ID", required = true) @RequestParam(required = true) Long serviceProviderId,
            @Parameter(description = "status", required = false) @RequestParam(required = false) JobLeadClaimStatus status,
            @Parameter(description = "Page number", required = false) @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size", required = false) @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort field", required = false) @RequestParam(defaultValue = "name") String sortBy,
            HttpServletRequest request
    ) {

        Pageable pageable = paginationService.getPagination(page, size, sortBy, JobLeadClaim.class);

        Page<JobLeadClaimSPListViewDto> pagedClaims = jobLeadClaimService.getClaimsByStatusForServiceProvider(serviceProviderId, status, pageable);

        PagedModel<EntityModel<JobLeadClaimSPListViewDto>> pagedModels = pagedSPListAssembler.toModel(
                pagedClaims,
                claim -> spListDtoModelAssembler.toModel(claim, request)
        );
        ApiResponse<PagedModel<EntityModel<JobLeadClaimSPListViewDto>>> response = ApiResponse.<PagedModel<EntityModel<JobLeadClaimSPListViewDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job leads retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModels)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{jobLeadClaimId}")
    @Operation(summary = "Get a job lead by ID for service provider", description = "Get a job lead by ID for service provider")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<EntityModel<JobLeadClaimDetailSPViewDto>>> getClaimByIdForServiceProvider(
            @Parameter(description = "Service Provider ID", required = true) @RequestParam(required = true) Long serviceProviderId,
            @Parameter(description = "Job lead claim ID", required = true) @PathVariable Long jobLeadClaimId,
            HttpServletRequest request
    ) {
        JobLeadClaimDetailSPViewDto claim = jobLeadClaimService.getClaimByIdForServiceProvider(serviceProviderId, jobLeadClaimId);
        EntityModel<JobLeadClaimDetailSPViewDto> model = spDetailDtoModelAssembler.toModel(claim, request);
        ApiResponse<EntityModel<JobLeadClaimDetailSPViewDto>> response = ApiResponse.<EntityModel<JobLeadClaimDetailSPViewDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job lead retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(model)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{jobLeadClaimId}")
    @Operation(summary = "Withdraw a job lead by Service Provider", description = "Update a job lead by Service Provider")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<Void>> withdrawJobLeadClaimByServiceProvider(
            @Parameter(description = "Service Provider ID", required = true) @RequestParam(required = true) Long serviceProviderId,
            @Parameter(description = "Job lead claim ID", required = true) @PathVariable Long jobLeadClaimId,
            HttpServletRequest request
    ) {
        jobLeadClaimService.withdrawJobLeadClaimByServiceProvider(serviceProviderId, jobLeadClaimId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job lead claim withdrawn successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/customer/{jobLeadClaimId}")
    @Operation(summary = "Get a job lead by ID for customer", description = "Get a job lead by ID for customer")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<EntityModel<JobLeadClaimDetailCustomerViewDto>>> getClaimByIdForCustomer(
            @Parameter(description = "Job lead claim ID", required = true) @PathVariable Long jobLeadClaimId,
            HttpServletRequest request
    ) {
        JobLeadClaimDetailCustomerViewDto claim = jobLeadClaimService.getClaimByIdForCustomer(jobLeadClaimId);
        EntityModel<JobLeadClaimDetailCustomerViewDto> model = detailCustomerViewDtoModelAssembler.toModel(claim, request);

        ApiResponse<EntityModel<JobLeadClaimDetailCustomerViewDto>> response = ApiResponse.<EntityModel<JobLeadClaimDetailCustomerViewDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job lead retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(model)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/customer/{jobLeadClaimId}/accept")
    @Operation(summary = "Accept a job lead claim by customer", description = "Accept a job lead claim by customer")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> acceptJobLeadClaimByCustomer(
            @Parameter(description = "Job lead claim ID", required = true) @PathVariable Long jobLeadClaimId,
            HttpServletRequest request
    ) {
        jobLeadClaimService.acceptJobLeadClaimByCustomer(jobLeadClaimId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job lead claim accepted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/customer/{jobLeadClaimId}/reject")
    @Operation(summary = "Reject a job lead by customer", description = "Reject a job lead by customer")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> rejectJobLeadClaimByCustomer(
            @Parameter(description = "Job lead claim ID", required = true) @PathVariable Long jobLeadClaimId,
            HttpServletRequest request
    ) {
        jobLeadClaimService.rejectJobLeadClaimByCustomer(jobLeadClaimId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job lead claim rejected successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/customer/{jobLeadClaimId}/requote-request")
    @Operation(summary = "Request requote for a job lead claim by customer", description = "Request requote for a job lead claim by customer")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> requestRequoteForJobLeadClaimByCustomer(
            @Parameter(description = "Job lead claim ID", required = true) @PathVariable Long jobLeadClaimId,
            HttpServletRequest request
    ) {
        jobLeadClaimService.requestRequoteForJobLeadClaimByCustomer(jobLeadClaimId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Requote request sent successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
