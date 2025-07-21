package com.muyategna.backend.job_request.controller.secured_public;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimCreateDto;
import com.muyategna.backend.job_request.service.JobLeadClaimService;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/secured/public/job-lead-claim")
@Tag(name = "Job Lead Claim", description = "Job Lead Claim Secured Public API")
public class JobLeadClaimSecuredPublicController {

    private final JobLeadClaimService jobLeadClaimService;

    @Autowired
    public JobLeadClaimSecuredPublicController(JobLeadClaimService jobLeadClaimService) {
        this.jobLeadClaimService = jobLeadClaimService;
    }

    @PostMapping
    @Operation(summary = "Claim a job lead", description = "Claim a job lead by service provider")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER", "EMPLOYEE"})
    public ResponseEntity<ApiResponse<Void>> claimJobLead(
            @Parameter(description = "Service Provider ID", required = true) @RequestParam Long serviceProviderId,
            @Parameter(description = "Job quote object", required = true) @Valid @RequestBody JobLeadClaimCreateDto jobLeadClaimCreateDto,
            HttpServletRequest request
    ) {
        if(!Objects.equals(serviceProviderId, jobLeadClaimCreateDto.getServiceProviderId())) {
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

}
