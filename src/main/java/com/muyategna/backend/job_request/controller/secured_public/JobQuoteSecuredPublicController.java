package com.muyategna.backend.job_request.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.assembler.JobQuoteDtoModelAssembler;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteDto;
import com.muyategna.backend.job_request.service.JobQuoteService;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/secured/public/job-leads/{claimId}/job-quotes")
@Tag(name = "Job Quote Controller", description = "Job Quote Controller - for creating job quotes by service providers")
public class JobQuoteSecuredPublicController {
    private final JobQuoteService jobQuoteService;
    private final JobQuoteDtoModelAssembler assembler;

    @Autowired
    public JobQuoteSecuredPublicController(JobQuoteService jobQuoteService, JobQuoteDtoModelAssembler assembler) {
        this.jobQuoteService = jobQuoteService;
        this.assembler = assembler;
    }

    @PostMapping
    @Operation(summary = "Create Job Quote", description = "Create Job Quote")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<EntityModel<JobQuoteDto>>> createNewJobQuoteAfterClaim(
            @Parameter(description = "Service Provider ID", required = true) @RequestParam Long serviceProviderId,
            @Parameter(description = "Job quote object", required = true) @Valid @RequestBody JobQuoteCreateDto jobQuoteCreateDto,
            HttpServletRequest request
    ) {

        JobQuoteDto jobQuote = jobQuoteService.createNewJobQuoteAfterClaim(jobQuoteCreateDto, serviceProviderId);
        EntityModel<JobQuoteDto> entityModel = assembler.toModel(jobQuote, request);
        ApiResponse<EntityModel<JobQuoteDto>> response = ApiResponse.<EntityModel<JobQuoteDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Job leads for current country retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/request-requote")
    @Operation(summary = "Customer Requote Request", description = "Customer Requote Request")
    public ResponseEntity<ApiResponse<Void>> createNewJobQuote(
            @Parameter(description = "Job lead claim id", required = true) @RequestParam Long leadClaimId,
            HttpServletRequest request
    ) {

        jobQuoteService.requestRequote(leadClaimId);
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
