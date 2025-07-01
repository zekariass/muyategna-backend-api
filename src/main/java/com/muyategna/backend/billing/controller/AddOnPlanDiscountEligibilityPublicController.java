package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.AddOnPlanEligibilityForDiscountPlanDtoModelPublicAssembler;
import com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility.AddOnPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.service.AddOnPlanDiscountEligibilityService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/billing/add-on-plan-discount-eligibility")
@Tag(name = "Add On Plan Discount Eligibility", description = "Add On Plan Discount Eligibility")
public class AddOnPlanDiscountEligibilityPublicController {

    private final AddOnPlanDiscountEligibilityService eligibilityService;
    private final AddOnPlanEligibilityForDiscountPlanDtoModelPublicAssembler assembler;

    @Autowired
    public AddOnPlanDiscountEligibilityPublicController(AddOnPlanDiscountEligibilityService eligibilityService,
                                                        AddOnPlanEligibilityForDiscountPlanDtoModelPublicAssembler assembler) {
        this.eligibilityService = eligibilityService;
        this.assembler = assembler;
    }


    @GetMapping("/{eligibilityId}")
    @Operation(summary = "Get eligibility by id", description = "Get eligibility by id")
    public ResponseEntity<ApiResponse<EntityModel<AddOnPlanDiscountEligibilityDto>>> getEligibilityById(
            @Parameter(description = "Id of the eligibility", required = true) @PathVariable Long eligibilityId,
            HttpServletRequest request) {
        AddOnPlanDiscountEligibilityDto eligibilityDto = eligibilityService.getAddOnPlanDiscountEligibilityById(eligibilityId);
        EntityModel<AddOnPlanDiscountEligibilityDto> collectionModel = assembler.toModel(eligibilityDto, request);

        ApiResponse<EntityModel<AddOnPlanDiscountEligibilityDto>> response = ApiResponse.<EntityModel<AddOnPlanDiscountEligibilityDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Eligibility with id: %d retrieved successfully".formatted(eligibilityId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(assembler.toModel(eligibilityDto, request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    @Operation(summary = "Get all eligibility for add on plan", description = "Get all eligibility for add on plan")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<AddOnPlanDiscountEligibilityDto>>>> getAllEligibility(
            @Parameter(description = "Id of the add on plan", required = true) @RequestParam Long addOnPlanId,
            HttpServletRequest request) {

        List<AddOnPlanDiscountEligibilityDto> eligibilityDtoList = eligibilityService.getAllAddOnPlanDiscountEligibilityByAddOnPlanId(addOnPlanId);
        CollectionModel<EntityModel<AddOnPlanDiscountEligibilityDto>> collectionModel = assembler.toCollectionModel(eligibilityDtoList, request);

        ApiResponse<CollectionModel<EntityModel<AddOnPlanDiscountEligibilityDto>>> response = ApiResponse.<CollectionModel<EntityModel<AddOnPlanDiscountEligibilityDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("All eligibility retrieved successfully for add on plan with id: %d".formatted(addOnPlanId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
