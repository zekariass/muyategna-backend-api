package com.muyategna.backend.professional_service.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.professional_service.assembler.ServiceCategoryDtoModelPublicAssembler;
import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryLocalizedDto;
import com.muyategna.backend.professional_service.service.ServiceCategoryService;
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
@RequestMapping("/api/v1/public/professional-service/service-categories")
@Tag(name = "Service Category Public Controller", description = "Controller for public access to service categories")
public class ServiceCategoryPublicController {

    private final ServiceCategoryService serviceCategoryService;
    private final ServiceCategoryDtoModelPublicAssembler assembler;

    @Autowired
    public ServiceCategoryPublicController(ServiceCategoryService serviceCategoryService, ServiceCategoryDtoModelPublicAssembler assembler) {
        this.serviceCategoryService = serviceCategoryService;
        this.assembler = assembler;
    }


    @GetMapping
    @Operation(summary = "Get all service categories", description = "Get all service categories")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ServiceCategoryLocalizedDto>>>> getAllServiceCategories(HttpServletRequest request) {

        List<ServiceCategoryLocalizedDto> serviceCategoriesDto = serviceCategoryService.getAllLocalizedServiceCategories();

        CollectionModel<EntityModel<ServiceCategoryLocalizedDto>> collectionModel = assembler.toCollectionModel(serviceCategoriesDto, request);
        ApiResponse<CollectionModel<EntityModel<ServiceCategoryLocalizedDto>>> response = ApiResponse.<CollectionModel<EntityModel<ServiceCategoryLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service categories retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{categoryId}")
    @Operation(summary = "Get service category by ID", description = "Get a specific service category by its ID")
    public ResponseEntity<ApiResponse<EntityModel<ServiceCategoryLocalizedDto>>> getServiceCategoryById(@Parameter(description = "Id of the service category to be retrieved", required = true) @PathVariable Long categoryId,
                                                                                                        HttpServletRequest request) {
        ServiceCategoryLocalizedDto serviceCategoryDto = serviceCategoryService.getLocalizedServiceCategoryById(categoryId);
        EntityModel<ServiceCategoryLocalizedDto> entityModel = assembler.toModel(serviceCategoryDto, request);
        ApiResponse<EntityModel<ServiceCategoryLocalizedDto>> response = ApiResponse.<EntityModel<ServiceCategoryLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service category with id: %d is retrieved successfully".formatted(categoryId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }
}
