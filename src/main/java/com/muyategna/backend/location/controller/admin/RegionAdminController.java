package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.AdminPaginationService;
import com.muyategna.backend.location.assembler.RegionDtoModelAdminAssembler;
import com.muyategna.backend.location.dto.region.RegionCreateDto;
import com.muyategna.backend.location.dto.region.RegionDto;
import com.muyategna.backend.location.dto.region.RegionUpdateDto;
import com.muyategna.backend.location.entity.Region;
import com.muyategna.backend.location.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/location/regions")
@Slf4j
@Tag(name = "Region Admin Management", description = "Endpoints related to region management")
@PreAuthorize("isAuthenticated() && hasAuthority('ROLE_ADMIN')")
public class RegionAdminController {
    private final RegionService regionService;
    private final AdminPaginationService adminPaginationService;
    private final PagedResourcesAssembler<RegionDto> pagedResourcesAssembler;
    private final RegionDtoModelAdminAssembler regionDtoAssembler;

    @Autowired
    public RegionAdminController(RegionService regionService, AdminPaginationService adminPaginationService, PagedResourcesAssembler<RegionDto> pagedResourcesAssembler, RegionDtoModelAdminAssembler regionDtoAssembler) {
        this.regionService = regionService;
        this.adminPaginationService = adminPaginationService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.regionDtoAssembler = regionDtoAssembler;
    }


    /**
     * Get all regions regardless of country.
     *
     * @param request the HTTP request object
     * @return a ResponseEntity containing an ApiResponse with a list of RegionDto objects
     */
    @Operation(summary = "Get all regions", description = "Get all regions regardless of country")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionDto>>> getAllRegions(HttpServletRequest request) {
        List<RegionDto> regions = regionService.getAllRegions();
        ApiResponse<List<RegionDto>> response = ApiResponse.<List<RegionDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Regions retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regions)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a paginated list of regions for a specified country.
     *
     * @param countryId the ID of the country to retrieve regions for
     * @param page      the page number to retrieve, defaults to 1 if not specified
     * @param size      the number of items per page, optional
     * @param sortBy    the field to sort the results by, defaults to "name" if not specified
     * @param request   the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a PagedModel of RegionDto entities as EntityModels
     */
    @Operation(summary = "Get regions by country", description = "Get regions by country")
    @GetMapping("/country/{countryId}")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<RegionDto>>>> getRegionsByCountry(@Parameter(description = "Id of the country to be retrieved", required = true) @PathVariable Long countryId,
                                                                                               @Parameter(description = "Page number", required = false) @RequestParam(defaultValue = "1") Integer page,
                                                                                               @Parameter(description = "Page size", required = false) @RequestParam(required = false) Integer size,
                                                                                               @Parameter(description = "Sort field", required = false) @RequestParam(defaultValue = "name") String sortBy,
                                                                                               HttpServletRequest request) {
        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, Region.class);
        Page<RegionDto> pagedRegions = regionService.getRegionsByCountry(countryId, pageable);

        PagedModel<EntityModel<RegionDto>> pagedModel = pagedResourcesAssembler
                .toModel(pagedRegions, (regionDto) -> regionDtoAssembler.toModel(regionDto,
                        adminPaginationService.getSafePage(page),
                        adminPaginationService.getSafeSize(size),
                        adminPaginationService.getSafeSortBy(sortBy, Region.class), request));


        ApiResponse<PagedModel<EntityModel<RegionDto>>> response = ApiResponse.<PagedModel<EntityModel<RegionDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Regions retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a region by its ID.
     *
     * @param regionId the ID of the region to be retrieved
     * @param request  the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a RegionDto object as an EntityModel
     */
    @GetMapping("/{regionId}")
    @Operation(summary = "Get region by ID", description = "Get region by ID")
    public ResponseEntity<ApiResponse<EntityModel<RegionDto>>> getRegionById(Long regionId, HttpServletRequest request) {
        RegionDto region = regionService.getRegionById(regionId);

        EntityModel<RegionDto> regionModel = regionDtoAssembler.toModel(region, request);

        ApiResponse<EntityModel<RegionDto>> response = ApiResponse.<EntityModel<RegionDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region with id: %d is retrieved successfully".formatted(regionId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regionModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Create a new region
     *
     * @param regionCreateDto the region object to be created
     * @param request         the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a newly created RegionDto object as an EntityModel
     */
    @PostMapping
    @Operation(summary = "Create a new region", description = "Create a new region")
    public ResponseEntity<ApiResponse<EntityModel<RegionDto>>> createRegion(@Parameter(description = "Region to be created", required = true) @Valid @RequestBody RegionCreateDto regionCreateDto, HttpServletRequest request) {
        RegionDto createdRegion = regionService.createRegion(regionCreateDto);

        EntityModel<RegionDto> regionModel = regionDtoAssembler.toModel(createdRegion, request);

        ApiResponse<EntityModel<RegionDto>> response = ApiResponse.<EntityModel<RegionDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Region created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regionModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Updates a region
     *
     * @param regionId        the id of the region to be updated
     * @param regionUpdateDto the region object to be updated
     * @param request         the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the updated RegionDto object as an EntityModel
     */
    @PutMapping("/{regionId}")
    @Operation(summary = "Update region", description = "Update region")
    public ResponseEntity<ApiResponse<EntityModel<RegionDto>>> updateRegion(@Parameter(description = "Id of the region to be updated", required = true) @PathVariable Long regionId,
                                                                            @Parameter(description = "Region to be updated", required = true) @Valid @RequestBody RegionUpdateDto regionUpdateDto,
                                                                            HttpServletRequest request) {
        RegionDto updatedRegion = regionService.updateRegion(regionId, regionUpdateDto);

        EntityModel<RegionDto> regionModel = regionDtoAssembler.toModel(updatedRegion, request);

        ApiResponse<EntityModel<RegionDto>> response = ApiResponse.<EntityModel<RegionDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region with id: %d is updated successfully".formatted(regionId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regionModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Deletes a region by id.
     *
     * @param regionId the id of the region to be deleted
     * @param request  the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a success message
     */
    @DeleteMapping("/{regionId}")
    @Operation(summary = "Delete region", description = "Delete region by id")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@Parameter(description = "Id of the region to be deleted", required = true) @PathVariable Long regionId, HttpServletRequest request) {
        regionService.deleteRegion(regionId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region with id: %d is deleted successfully".formatted(regionId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
