package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.SubscriptionPlanLocalizedDtoModelPublicAssembler;
import com.muyategna.backend.billing.dto.subscription_plan.SubscriptionPlanLocalizedDto;
import com.muyategna.backend.billing.service.SubscriptionPlanService;
import com.muyategna.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/billing/subscription-plans")
@Tag(name = "Subscription Plan", description = "The Subscription Plan API")
public class SubscriptionPlanPublicController {
    private final SubscriptionPlanService subscriptionPlanService;
    private final SubscriptionPlanLocalizedDtoModelPublicAssembler assembler;

    public SubscriptionPlanPublicController(SubscriptionPlanService subscriptionPlanService, SubscriptionPlanLocalizedDtoModelPublicAssembler assembler) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.assembler = assembler;
    }


    @GetMapping
    @Operation(summary = "Get all subscription plans for a service category", description = "Get all subscription plans for a service category")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<SubscriptionPlanLocalizedDto>>>> getAllSubscriptionPlansForCurrentCountry(
            HttpServletRequest request) {

        List<SubscriptionPlanLocalizedDto> subscriptionPlanDtoList = subscriptionPlanService.getAllSubscriptionPlansForCurrentCountry();
        CollectionModel<EntityModel<SubscriptionPlanLocalizedDto>> pagedModel = assembler.toCollectionModel(subscriptionPlanDtoList, request);

        ApiResponse<CollectionModel<EntityModel<SubscriptionPlanLocalizedDto>>> response = ApiResponse.<CollectionModel<EntityModel<SubscriptionPlanLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Subscription plans retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{subscriptionPlanId}")
    public ResponseEntity<ApiResponse<EntityModel<SubscriptionPlanLocalizedDto>>> getSubscriptionPlanById(
            @Parameter(description = "Id of the subscription plan", required = true) @PathVariable Long subscriptionPlanId,
            HttpServletRequest request) {

        SubscriptionPlanLocalizedDto subscriptionPlanDto = subscriptionPlanService.getSubscriptionPlanById(subscriptionPlanId);
        EntityModel<SubscriptionPlanLocalizedDto> model = assembler.toModel(subscriptionPlanDto, request);

        ApiResponse<EntityModel<SubscriptionPlanLocalizedDto>> response = ApiResponse.<EntityModel<SubscriptionPlanLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Subscription plan retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(model)
                .build();
        return ResponseEntity.ok(response);
    }
}
