package com.muyategna.backend.job_request.controller.secured_public;


import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.job_request.service.InviteForQuoteService;
import com.muyategna.backend.service_provider.annotation.ServiceProviderSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/secured/public/invite-for-quote")
@Tag(name = "Invite For Quote", description = "Invite For Quote")
public class InviteForQuoteSecuredPublicController {

    private final InviteForQuoteService inviteForQuiteService;

    @Autowired
    public InviteForQuoteSecuredPublicController(InviteForQuoteService inviteForQuiteService) {
        this.inviteForQuiteService = inviteForQuiteService;
    }


    @PostMapping
    @Operation(summary = "Invite For Quote", description = "Invite For Quote")
    public ResponseEntity<ApiResponse<Void>> inviteForQuote(
            @Parameter(description = "Job Request Id", required = true) @RequestParam Long jobRequestId,
            @Parameter(description = "Service Provider ID", required = true) @RequestParam Long serviceProviderId,
            HttpServletRequest request
    ) {

        inviteForQuiteService.inviteForQuote(jobRequestId, serviceProviderId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Invite for quote sent successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{inviteForQuoteId}/view")
    @Operation(summary = "View the invite", description = "View the invite")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<Void>> viewTheInviteForQuote(
            @Parameter(description = "Service provider id", required = true) @RequestParam Long serviceProviderId,
            @Parameter(description = "Invite For Quote Id", required = true) @PathVariable Long inviteForQuoteId,
            HttpServletRequest request
    ) {

        inviteForQuiteService.viewTheInviteForQuote(serviceProviderId, inviteForQuoteId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("The invite is viewed successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{inviteForQuoteId}/accept")
    @Operation(summary = "Accept the invite", description = "Accept the invite")
    @ServiceProviderSecurity(requiredRoles = {"MANAGER", "OWNER"})
    public ResponseEntity<ApiResponse<Void>> acceptTheInviteForQuote(
            @Parameter(description = "Service provider id", required = true) @RequestParam Long serviceProviderId,
            @Parameter(description = "Invite For Quote Id", required = true) @PathVariable Long inviteForQuoteId,
            HttpServletRequest request
    ) {

        inviteForQuiteService.acceptTheInviteForQuote(serviceProviderId, inviteForQuoteId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Invite for quote accepted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
