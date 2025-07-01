package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.SubscriptionPlanEligibilityForDiscountPlanDtoModelPublicAssembler;
import com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility.SubscriptionPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.service.SubscriptionPlanDiscountEligibilityService;
import com.muyategna.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/billing/subscription-plan-discount-eligibility")
@Tag(name = "Subscription Plan Discount Eligibility", description = "Subscription Plan Discount Eligibility endpoints")
public class SubscriptionPlanDiscountEligibilityPublicController {

    private final SubscriptionPlanDiscountEligibilityService eligibilityService;
    private final SubscriptionPlanEligibilityForDiscountPlanDtoModelPublicAssembler assembler;

    @Autowired
    public SubscriptionPlanDiscountEligibilityPublicController(SubscriptionPlanDiscountEligibilityService eligibilityService,
                                                               SubscriptionPlanEligibilityForDiscountPlanDtoModelPublicAssembler assembler) {
        this.eligibilityService = eligibilityService;
        this.assembler = assembler;
    }


    @GetMapping("/{eligibilityId}")
    @Operation(summary = "Get eligibility by id", description = "Get eligibility by id")
    public ResponseEntity<ApiResponse<EntityModel<SubscriptionPlanDiscountEligibilityDto>>> getEligibilityById(
            @Parameter(description = "Id of the eligibility", required = true) @PathVariable Long eligibilityId,
            HttpServletRequest request) {

        SubscriptionPlanDiscountEligibilityDto eligibilityDto = eligibilityService.getSubscriptionPlanDiscountEligibilityById(eligibilityId);
        EntityModel<SubscriptionPlanDiscountEligibilityDto> collectionModel = assembler.toModel(eligibilityDto, request);
        ApiResponse<EntityModel<SubscriptionPlanDiscountEligibilityDto>> response = ApiResponse.<EntityModel<SubscriptionPlanDiscountEligibilityDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Eligibility with id: %d retrieved successfully".formatted(eligibilityId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    @Operation(summary = "Get subscription plan eligibilities by subscription plan id", description = "Get eligibilities by subscription plan id")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<SubscriptionPlanDiscountEligibilityDto>>>> getEligibilitiesBySubscriptionPlanId(
            @Parameter(description = "Id of the subscription plan", required = true) @RequestParam Long subscriptionPlanId,
            HttpServletRequest request) {

        Iterable<SubscriptionPlanDiscountEligibilityDto> eligibilityDtoList = eligibilityService.getAllSubscriptionPlanEligibilityBySubscriptionPlanId(subscriptionPlanId);
        CollectionModel<EntityModel<SubscriptionPlanDiscountEligibilityDto>> collectionModel = assembler.toCollectionModel(eligibilityDtoList, request);

        ApiResponse<CollectionModel<EntityModel<SubscriptionPlanDiscountEligibilityDto>>> response = ApiResponse.<CollectionModel<EntityModel<SubscriptionPlanDiscountEligibilityDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Subscription Plan Eligibility for subscription plan with id: %d retrieved successfully".formatted(subscriptionPlanId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
