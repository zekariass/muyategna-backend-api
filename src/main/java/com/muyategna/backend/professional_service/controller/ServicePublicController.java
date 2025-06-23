package com.muyategna.backend.professional_service.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.PublicPaginationService;
import com.muyategna.backend.professional_service.assembler.ServiceDtoModelPublicAssembler;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.professional_service.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/public/professional-service/services")
@Tag(name = "Service Public Controller", description = "Controller for public access to services")
public class ServicePublicController {

    private final ServiceService serviceService;
    private final ServiceDtoModelPublicAssembler assembler;
    private final PagedResourcesAssembler<ServiceLocalizedDto> pagedResourcesAssembler;
    private final PublicPaginationService publicPaginationService;

    public ServicePublicController(ServiceService serviceService, ServiceDtoModelPublicAssembler assembler, PagedResourcesAssembler<ServiceLocalizedDto> pagedResourcesAssembler, PublicPaginationService publicPaginationService) {
        this.serviceService = serviceService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.publicPaginationService = publicPaginationService;
    }

    //Uncomment the following method if you want to implement paginated services by category
//
//    @GetMapping("/category/paged/{categoryId}")
//    @Operation(summary = "Get all services by category for current country", description = "Get all services filtered by category for the current country")
//    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ServiceLocalizedDto>>>> getPagedServicesByCategoryForCurrentCountry(
//            @Parameter(description = "Id of the service category to filter services") @PathVariable Long categoryId,
//            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
//            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
//            @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "id") String sortBy,
//            HttpServletRequest request) {
//
//
//        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, Service.class);
//        Page<ServiceLocalizedDto> serviceDtoList = serviceService.getPagedServicesByCategoryForCurrentCountry(categoryId, pageable);
//        PagedModel<EntityModel<ServiceLocalizedDto>> pagedModel = pagedResourcesAssembler.toModel(
//                serviceDtoList,
//                dto -> assembler.toModel(dto, request)
//        );
//
//        ApiResponse<PagedModel<EntityModel<ServiceLocalizedDto>>> response = ApiResponse.<PagedModel<EntityModel<ServiceLocalizedDto>>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Services for category with id: %d retrieved successfully".formatted(categoryId))
//                .path(request.getRequestURI())
//                .timestamp(LocalDateTime.now())
//                .data(pagedModel)
//                .build();
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all services by category for current country", description = "Get all services filtered by category for the current country")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ServiceLocalizedDto>>>> getServicesByCategoryForCurrentCountry(
            @Parameter(description = "Id of the service category to filter services") @PathVariable Long categoryId,
            HttpServletRequest request) {

        List<ServiceLocalizedDto> pagedServiceDto = serviceService.getServicesByCategoryForCurrentCountry(categoryId);
        CollectionModel<EntityModel<ServiceLocalizedDto>> pagedModel = assembler.toCollectionModel(
                pagedServiceDto, request
        );

        ApiResponse<CollectionModel<EntityModel<ServiceLocalizedDto>>> response = ApiResponse.<CollectionModel<EntityModel<ServiceLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Services for category with id: %d retrieved successfully".formatted(categoryId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping
    @Operation(summary = "Get all services for current country", description = "Get all services for the current country")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ServiceLocalizedDto>>>> getAllServicesForCurrentCountry(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
            @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "id") String sortBy,
            HttpServletRequest request) {

        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, Service.class);
        Page<ServiceLocalizedDto> serviceDtoList = serviceService.getAllServicesForCurrentCountry(pageable);

        PagedModel<EntityModel<ServiceLocalizedDto>> pagedModel = pagedResourcesAssembler.toModel(
                serviceDtoList,
                dto -> assembler.toModel(dto, request)
        );

        ApiResponse<PagedModel<EntityModel<ServiceLocalizedDto>>> response = ApiResponse.<PagedModel<EntityModel<ServiceLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Services for current country retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{serviceId}")
    @Operation(summary = "Get a specific service by its ID ", description = "Get a specific service by its ID for the current country")
    public ResponseEntity<ApiResponse<EntityModel<ServiceLocalizedDto>>> getServiceByIdForCurrentCountry(@Parameter(description = "Id of the service to be retrieved", required = true) @PathVariable Long serviceId,
                                                                                                         HttpServletRequest request) {
        ServiceLocalizedDto serviceDto = serviceService.getServiceByIdForCurrentCountry(serviceId);
        EntityModel<ServiceLocalizedDto> entityModel = assembler.toModel(serviceDto, request);
        ApiResponse<EntityModel<ServiceLocalizedDto>> response = ApiResponse.<EntityModel<ServiceLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Service with id: %d is retrieved successfully".formatted(serviceId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/slug/{serviceName}")
    @Operation(summary = "Get unpaginated service by it name string", description = "Get unpaginated service by its name for the current country")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ServiceLocalizedDto>>>> getServicesByNameForCurrentCountry(@Parameter(description = "Id of the service to be retrieved", required = true) @RequestParam String serviceName,
                                                                                                                             @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
                                                                                                                             @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
                                                                                                                             @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "id") String sortBy,
                                                                                                                             HttpServletRequest request) {
        List<ServiceLocalizedDto> serviceDto = serviceService.getServicesByNameForCurrentCountry(serviceName);
        CollectionModel<EntityModel<ServiceLocalizedDto>> pagedModels = assembler.toCollectionModel(serviceDto, request);

        ApiResponse<CollectionModel<EntityModel<ServiceLocalizedDto>>> response = ApiResponse.<CollectionModel<EntityModel<ServiceLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Services with name: %s has been retrieved successfully".formatted(serviceName))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModels)
                .build();
        return ResponseEntity.ok(response);
    }


// Uncomment the following method if you want to implement paginated services by name
//
//    @GetMapping("/slug/paged/{serviceName}")
//    @Operation(summary = "Get paginated services by name", description = "Get paginated services by name for the current country")
//    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ServiceLocalizedDto>>>> getPagedServicesByNameForCurrentCountry(@Parameter(description = "Id of the service to be retrieved", required = true) @RequestParam String serviceName,
//                                                                                                                               @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
//                                                                                                                               @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
//                                                                                                                               @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "id") String sortBy,
//                                                                                                                               HttpServletRequest request) {
//        Pageable pageable = publicPaginationService.getPagination(page, size, sortBy, Service.class);
//        Page<ServiceLocalizedDto> serviceDto = serviceService.getPagedServicesByNameForCurrentCountry(serviceName, pageable);
//        PagedModel<EntityModel<ServiceLocalizedDto>> pagedModels = pagedResourcesAssembler.toModel(
//                serviceDto,
//                dto -> assembler.toModel(dto, request));
//
//        ApiResponse<PagedModel<EntityModel<ServiceLocalizedDto>>> response = ApiResponse.<PagedModel<EntityModel<ServiceLocalizedDto>>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .success(true)
//                .message("Service with name: %s has been retrieved successfully".formatted(serviceName))
//                .path(request.getRequestURI())
//                .timestamp(LocalDateTime.now())
//                .data(pagedModels)
//                .build();
//        return ResponseEntity.ok(response);
//    }


}
