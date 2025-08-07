package com.muyategna.backend.job_request.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.PublicPaginationService;
import com.muyategna.backend.job_request.assembler.JobLeadClaimListCustomerViewDtoModelAssembler;
import com.muyategna.backend.job_request.assembler.JobLeadListDtoModelAssembler;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.service.JobLeadService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import com.muyategna.backend.system.context.CountryContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/secured/public/job-leads")
@Tag(name = "Job Lead Secured Public Controller", description = "Job Lead Secured Public API")
public class JobLeadSecuredPublicController {
    private final JobLeadService jobLeadService;
    private final JobLeadListDtoModelAssembler listAssembler;
    private final PagedResourcesAssembler<JobLeadListDto> pagedAssembler;
    private final JobLeadClaimListCustomerViewDtoModelAssembler claimCustomerListDtoModelAssembler;
    private final PublicPaginationService publicPaginationService;

    @Autowired
    public JobLeadSecuredPublicController(JobLeadService jobLeadService, JobLeadListDtoModelAssembler listAssembler, PagedResourcesAssembler<JobLeadListDto> pagedAssembler, JobLeadClaimListCustomerViewDtoModelAssembler claimCustomerListDtoModelAssembler, PublicPaginationService publicPaginationService) {
        this.jobLeadService = jobLeadService;
        this.listAssembler = listAssembler;
        this.pagedAssembler = pagedAssembler;
        this.claimCustomerListDtoModelAssembler = claimCustomerListDtoModelAssembler;
        this.publicPaginationService = publicPaginationService;
    }

    @GetMapping
    @Operation(summary = "Get all job leads for current country", description = "Get all job leads for the current country")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER", "EMPLOYEE"})
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<JobLeadListDto>>>> getJobLeadsClosestToTheServiceProvider(
            @Parameter(description = "Service Provider ID") @RequestParam Long serviceProviderId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt,desc") String sortBy,
            HttpServletRequest request) {
        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, JobLead.class);
        CountryDto currentCountryDto = CountryContextHolder.getCountry();
        Page<JobLeadListDto> jobLeadDtoList = jobLeadService.getJobLeadsClosestToTheServiceProvider(serviceProviderId, pageable);

        PagedModel<EntityModel<JobLeadListDto>> pagedModel = pagedAssembler
                .toModel(jobLeadDtoList, lead -> listAssembler.toModel(lead,
                        serviceProviderId,
                        publicPaginationService.getSafePage(page),
                        publicPaginationService.getSafeSize(size),
                        publicPaginationService.getSafeSortBy(sortBy, JobLead.class),
                        request));

        ApiResponse<PagedModel<EntityModel<JobLeadListDto>>> response = ApiResponse.<PagedModel<EntityModel<JobLeadListDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Job leads for current country retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{jobLeadId}/claims")
    @Operation(summary = "Get all claims of a job lead for customer", description = "Get all claims of a job lead for customer")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>>>> getClaimsOfJobLeadForCustomer(
            @Parameter(description = "Job lead ID", required = true) @PathVariable Long jobLeadId,
            HttpServletRequest request
    ) {

        List<JobLeadClaimListCustomerViewDto> claimListViewDto = jobLeadService.getAllClaimsForJobLead(jobLeadId);

        CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>> pagedModel = claimCustomerListDtoModelAssembler.toCollectionModel(claimListViewDto, request);
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
