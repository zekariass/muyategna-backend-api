package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.AddOnPlanDtoModelPublicAssembler;
import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import com.muyategna.backend.billing.service.AddOnPlanService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/billing/add-on-plans")
@Tag(name = "AddOnPlan API", description = "AddOnPlan APIs")
public class AddOnPlanPublicController {
    private final AddOnPlanDtoModelPublicAssembler assembler;
    private final AddOnPlanService addOnPlanService;

    @Autowired
    public AddOnPlanPublicController(AddOnPlanDtoModelPublicAssembler assembler, AddOnPlanService addOnPlanService) {
        this.assembler = assembler;
        this.addOnPlanService = addOnPlanService;
    }


    @GetMapping("/{planId}")
    @Operation(summary = "Get AddOnPlan by ID", description = "Get AddOnPlan by ID")
    public ResponseEntity<ApiResponse<EntityModel<AddOnPlanLocalizedDto>>> getAddOnPlanById(@Parameter(description = "ID of the AddOnPlan") @PathVariable Long planId,
                                                                                            HttpServletRequest request) {
        AddOnPlanLocalizedDto addOnPlanLocalizedDto = addOnPlanService.getAddOnPlanById(planId);
        EntityModel<AddOnPlanLocalizedDto> entityModel = assembler.toModel(addOnPlanLocalizedDto, request);

        ApiResponse<EntityModel<AddOnPlanLocalizedDto>> apiResponse = ApiResponse.<EntityModel<AddOnPlanLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("AddOnPlan fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping
    @Operation(summary = "Get AddOnPlan by Service ID", description = "Get AddOnPlan by Service ID")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<AddOnPlanLocalizedDto>>>> getAddOnPlanByCountryId(
            HttpServletRequest request) {
        List<AddOnPlanLocalizedDto> addOnPlanDto = addOnPlanService.getAddOnPlansByCountryId();
        CollectionModel<EntityModel<AddOnPlanLocalizedDto>> entityModel = assembler.toCollectionModel(addOnPlanDto, request);
        ApiResponse<CollectionModel<EntityModel<AddOnPlanLocalizedDto>>> apiResponse = ApiResponse.<CollectionModel<EntityModel<AddOnPlanLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("AddOnPlans fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
