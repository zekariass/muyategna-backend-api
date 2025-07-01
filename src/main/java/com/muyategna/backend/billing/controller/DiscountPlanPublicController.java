package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.DiscountPlanDtoModelPublicAssembler;
import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import com.muyategna.backend.billing.service.DiscountPlanService;
import com.muyategna.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/billing/discount_plans")
@Tag(name = "Discount Plan Public Controller", description = "Discount Plan Public Controller")
public class DiscountPlanPublicController {

    private final DiscountPlanService discountPlanService;
    private final DiscountPlanDtoModelPublicAssembler assembler;

    @Autowired
    public DiscountPlanPublicController(DiscountPlanService discountPlanService, DiscountPlanDtoModelPublicAssembler assembler) {
        this.discountPlanService = discountPlanService;
        this.assembler = assembler;
    }

    @GetMapping("/{discountPlanId}")
    @Operation(summary = "Get discount plan by id", description = "Get discount plan by id")
    public ResponseEntity<ApiResponse<EntityModel<DiscountPlanLocalizedDto>>> getDiscountPlanById(@PathVariable Long discountPlanId, HttpServletRequest request) {
        DiscountPlanLocalizedDto discountPlanDto = discountPlanService.getDiscountPlanById(discountPlanId);
        ApiResponse<EntityModel<DiscountPlanLocalizedDto>> response = ApiResponse.<EntityModel<DiscountPlanLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Discount plan with id: %d retrieved successfully".formatted(discountPlanId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(assembler.toModel(discountPlanDto, request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
