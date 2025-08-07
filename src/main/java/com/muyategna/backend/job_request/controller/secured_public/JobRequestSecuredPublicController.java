package com.muyategna.backend.job_request.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.assembler.JobLeadClaimListCustomerViewDtoModelAssembler;
import com.muyategna.backend.job_request.assembler.JobRequestDetailDtoModelAssembler;
import com.muyategna.backend.job_request.assembler.JobRequestDtoModelAssembler;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestByExistingUserWithJsonStringCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDetailDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestUpdateDto;
import com.muyategna.backend.job_request.service.JobRequestService;
import com.muyategna.backend.service_provider.assembler.ServiceProviderListDtoModelPublicAssembler;
import com.muyategna.backend.service_provider.dto.service_provider.NearbyServiceProviderListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/secured/public/job-requests")
@Tag(name = "Job Request Secured Public Controller", description = "Job Request Secured Public API")
public class JobRequestSecuredPublicController {

    private final JobRequestService jobRequestService;
    private final JobRequestDtoModelAssembler assembler;
    private final JobRequestDetailDtoModelAssembler detailAssembler;
    private final ServiceProviderListDtoModelPublicAssembler providerListAssembler;
    private final JobLeadClaimListCustomerViewDtoModelAssembler claimListCustomerViewDtoModelAssembler;

    @Autowired
    public JobRequestSecuredPublicController(JobRequestService jobRequestService,
                                             JobRequestDtoModelAssembler assembler,
                                             JobRequestDetailDtoModelAssembler detailAssembler,
                                             ServiceProviderListDtoModelPublicAssembler providerListAssembler, JobLeadClaimListCustomerViewDtoModelAssembler claimListCustomerViewDtoModelAssembler) {
        this.jobRequestService = jobRequestService;
        this.assembler = assembler;
        this.detailAssembler = detailAssembler;
        this.providerListAssembler = providerListAssembler;
        this.claimListCustomerViewDtoModelAssembler = claimListCustomerViewDtoModelAssembler;
    }


    @GetMapping("/{jobRequestId}")
    @Operation(summary = "Get Job Request", description = "Get Job Request")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<EntityModel<JobRequestDetailDto>>> getJobRequestById(
            @Parameter(description = "Job Request Id") @PathVariable Long jobRequestId,
            HttpServletRequest request) {
        JobRequestDetailDto jobRequestDto = jobRequestService.getJobRequestDetailById(jobRequestId);
        EntityModel<JobRequestDetailDto> entityModel = detailAssembler.toModel(jobRequestDto, request);
        ApiResponse<EntityModel<JobRequestDetailDto>> response = ApiResponse.<EntityModel<JobRequestDetailDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Job request found successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Creates a new job request for existing customer.
     *
     * @param jsonStringDto The job request create DTO.
     * @param request       The HTTP request.
     * @return The created job request DTO.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create Job Request", description = "Create Job Request")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<EntityModel<JobRequestDto>>> createJobRequestForExistingUser(
            @Parameter(description = "Job Request Create DTO") @Valid @ModelAttribute JobRequestByExistingUserWithJsonStringCreateDto jsonStringDto,
            HttpServletRequest request) {

        JobRequestDto jobRequestDto = jobRequestService.createJobRequestForExistingUser(jsonStringDto);
        EntityModel<JobRequestDto> entityModel = assembler.toModel(jobRequestDto, request);
        ApiResponse<EntityModel<JobRequestDto>> response = ApiResponse.<EntityModel<JobRequestDto>>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Job request created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{jobRequestId}")
    @Operation(summary = "Update Job Request", description = "Update Job Request")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<EntityModel<JobRequestDto>>> updateJobRequest(
            @Parameter(description = "Job Request Id") @PathVariable Long jobRequestId,
            @Parameter(description = "Updated Job Request") @Valid @RequestBody JobRequestUpdateDto updateDto,
            HttpServletRequest request
    ) {
        if (!Objects.equals(jobRequestId, updateDto.getId())) {
            return ResponseEntity.badRequest().body(ApiResponse.<EntityModel<JobRequestDto>>builder()
                    .success(false)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Job request id mismatched")
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        JobRequestDto jobRequestDto = jobRequestService.updateJobRequest(jobRequestId, updateDto);
        EntityModel<JobRequestDto> entityModel = assembler.toModel(jobRequestDto, request);
        ApiResponse<EntityModel<JobRequestDto>> response = ApiResponse.<EntityModel<JobRequestDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Job request updated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{jobRequestId}/cancel")
    @Operation(summary = "Cancel Job Request", description = "Cancel Job Request")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> cancelJobRequest(
            @Parameter(description = "Job Request Id") @PathVariable Long jobRequestId,
            HttpServletRequest request
    ) {
        jobRequestService.cancelJobRequest(jobRequestId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Job request cancelled successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{jobRequestId}/reopen")
    @Operation(summary = "Reopen Job Request", description = "Reopen Job Request")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> reopenJobRequest(
            @Parameter(description = "Job Request Id") @PathVariable Long jobRequestId,
            HttpServletRequest request
    ) {
        jobRequestService.reopenJobRequest(jobRequestId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Job request reopened successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{jobRequestId}/nearby-service-providers")
    @Operation(summary = "Get Nearby Service Providers", description = "Get Nearby Service Providers")
    @PreAuthorize("isAuthenticated() and hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<NearbyServiceProviderListDto>>>> getNearbyServiceProvidersForJobRequest(
            @Parameter(description = "Job Request Id") @PathVariable Long jobRequestId,
            HttpServletRequest request) {

        List<NearbyServiceProviderListDto> nearbyServiceProviderListDtoList = jobRequestService.getNearbyServiceProvidersForJobRequest(jobRequestId);
        CollectionModel<EntityModel<NearbyServiceProviderListDto>> entityModel = providerListAssembler.toCollectionModel(nearbyServiceProviderListDtoList, request);
        ApiResponse<CollectionModel<EntityModel<NearbyServiceProviderListDto>>> response = ApiResponse.<CollectionModel<EntityModel<NearbyServiceProviderListDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Nearby service providers fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{jobRequestId}/claims")
    @Operation(summary = "Get all claims of a job request for customer", description = "Get all claims of a job request for customer")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>>>> getClaimsOfJobRequestForCustomer(
            @Parameter(description = "Job Request ID", required = true) @PathVariable Long jobRequestId,
            HttpServletRequest request
    ) {

        List<JobLeadClaimListCustomerViewDto> claimListViewDto = jobRequestService.getAllClaimsOfJobRequestForCustomer(jobRequestId);

        CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>> pagedModel = claimListCustomerViewDtoModelAssembler.toCollectionModel(claimListViewDto, request);
        ApiResponse<CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>>> response = ApiResponse.<CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job leads retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }

}
