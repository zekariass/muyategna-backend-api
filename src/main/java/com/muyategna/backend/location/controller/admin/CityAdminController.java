package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.AdminPaginationService;
import com.muyategna.backend.location.assembler.CityDtoModelAdminAssembler;
import com.muyategna.backend.location.dto.city.CityCreateDto;
import com.muyategna.backend.location.dto.city.CityDto;
import com.muyategna.backend.location.dto.city.CityUpdateDto;
import com.muyategna.backend.location.entity.City;
import com.muyategna.backend.location.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/admin/location/cities")
@Tag(name = "City Admin Management", description = "City Admin Controller")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class CityAdminController {
    private final CityService cityService;
    private final AdminPaginationService adminPaginationService;

    private final CityDtoModelAdminAssembler cityDtoAssembler;
    private final PagedResourcesAssembler<CityDto> pagedResourcesAssembler;

    @Autowired
    public CityAdminController(CityService cityService, AdminPaginationService adminPaginationService, CityDtoModelAdminAssembler cityAssembler, PagedResourcesAssembler<CityDto> pagedResourcesAssembler) {
        this.cityService = cityService;
        this.adminPaginationService = adminPaginationService;
        this.cityDtoAssembler = cityAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }


    /**
     * Retrieves a paginated list of cities for a given region.
     *
     * @param regionId The ID of the region for which cities are to be retrieved.
     * @param page     The page number to retrieve, default is 1.
     * @param size     The number of records per page, if not specified, the default size will be used.
     * @param sortBy   The field by which the results should be sorted, default is 'name'.
     * @param request  The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with a PagedModel of EntityModel<CityDto> objects.
     */
    @GetMapping("/region/{regionId}")
    @Operation(summary = "Get all cities by region", description = "Get all cities by region")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<CityDto>>>> getAllCitiesByRegion(@Parameter(description = "Id of the region", required = true) @PathVariable Long regionId,
                                                                                              @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
                                                                                              @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
                                                                                              @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
                                                                                              HttpServletRequest request) {
        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, City.class);
        Page<CityDto> pagedCities = cityService.getAllCitiesByRegionForAdmin(regionId, pageable);

        PagedModel<EntityModel<CityDto>> pagedModel = pagedResourcesAssembler
                .toModel(pagedCities, cityDto -> cityDtoAssembler.toModel(cityDto,
                        adminPaginationService.getSafePage(page),
                        adminPaginationService.getSafeSize(size),
                        adminPaginationService.getSafeSortBy(sortBy, City.class), request));

        ApiResponse<PagedModel<EntityModel<CityDto>>> response = ApiResponse.<PagedModel<EntityModel<CityDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Cities retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a city by id and language.
     *
     * @param cityId  Id of the city to be retrieved
     * @param request The HTTP request
     * @return The city
     */
    @GetMapping("/{cityId}")
    @Operation(summary = "Get city by id and language", description = "Get city by id and language")
    public ResponseEntity<ApiResponse<EntityModel<CityDto>>> getCityById(@Parameter(description = "Id of the city to be retrieved", required = true) @PathVariable Long cityId,
                                                                         HttpServletRequest request) {
        CityDto city = cityService.getCityByIdForAdmin(cityId);

        EntityModel<CityDto> cityModel = cityDtoAssembler.toModel(city, adminPaginationService.getSafePage(1),
                adminPaginationService.getSafeSize(10), adminPaginationService.getSafeSortBy("name", City.class), request);

        ApiResponse<EntityModel<CityDto>> response = ApiResponse.<EntityModel<CityDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(cityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a paginated list of all cities in the system.
     *
     * @param page    The page number to retrieve, default is 1.
     * @param size    The number of records per page, if not specified, the default size will be used.
     * @param sortBy  The field by which the results should be sorted, default is 'name'.
     * @param request The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with a PagedModel of EntityModel<CityDto> objects.
     */
    @GetMapping
    @Operation(summary = "Get all cities", description = "Get all cities in the system")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<CityDto>>>> getAllCities(@Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
                                                                                      @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
                                                                                      @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
                                                                                      HttpServletRequest request) {
        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, City.class);
        Page<CityDto> pagedCities = cityService.getAllCitiesForAdmin(pageable);

        // Use Spring HATEOAS to wrap paginated results with navigation links
        PagedModel<EntityModel<CityDto>> pagedModel = pagedResourcesAssembler
                .toModel(pagedCities, (cityDto) ->
                        cityDtoAssembler.toModel(cityDto,
                                adminPaginationService.getSafePage(page),
                                adminPaginationService.getSafeSize(size),
                                adminPaginationService.getSafeSortBy(sortBy, City.class), request));

        ApiResponse<PagedModel<EntityModel<CityDto>>> response = ApiResponse.<PagedModel<EntityModel<CityDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Cities retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Creates a new city in the system.
     *
     * @param cityCreateDto The city to be created.
     * @param request       The HTTP request.
     * @return A ResponseEntity containing an ApiResponse with the newly created city.
     */
    @PostMapping
    @Operation(summary = "Create a new city", description = "Create a new city")
    public ResponseEntity<ApiResponse<EntityModel<CityDto>>> addNewCity(@Parameter(description = "City to be created", required = true) @Valid @RequestBody CityCreateDto cityCreateDto,
                                                                        HttpServletRequest request) {

        CityDto createdCity = cityService.addNewCityByAdmin(cityCreateDto);

        EntityModel<CityDto> cityModel = cityDtoAssembler.toModel(createdCity, adminPaginationService.getSafePage(1),
                adminPaginationService.getSafeSize(10), adminPaginationService.getSafeSortBy("id", City.class), request);

        ApiResponse<EntityModel<CityDto>> response = ApiResponse.<EntityModel<CityDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("City created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(cityModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Updates an existing city based on the provided city ID and update details.
     *
     * @param cityId        The ID of the city to be updated.
     * @param cityUpdateDto The details of the city to be updated.
     * @param request       The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with the updated city details.
     */
    @PutMapping("/{cityId}")
    @Operation(summary = "Update a city", description = "Update a city")
    public ResponseEntity<ApiResponse<EntityModel<CityDto>>> updateCity(@Parameter(description = "Id of the city to be updated", required = true) @PathVariable Long cityId,
                                                                        @Parameter(description = "City to be updated", required = true) @Valid @RequestBody CityUpdateDto cityUpdateDto,
                                                                        HttpServletRequest request) {
        CityDto updatedCity = cityService.updateCityByAdmin(cityId, cityUpdateDto);

        EntityModel<CityDto> cityModel = cityDtoAssembler.toModel(updatedCity, adminPaginationService.getSafePage(1),
                adminPaginationService.getSafeSize(10), adminPaginationService.getSafeSortBy("id", City.class), request);

        ApiResponse<EntityModel<CityDto>> response = ApiResponse.<EntityModel<CityDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City updated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(cityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Deletes a city based on the provided city ID.
     *
     * @param cityId  The ID of the city to be deleted.
     * @param request The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with a success message.
     */
    @DeleteMapping("/{cityId}")
    @Operation(summary = "Delete a city", description = "Delete a city")
    public ResponseEntity<ApiResponse<CityDto>> deleteCity(@Parameter(description = "Id of the city to be deleted", required = true) @PathVariable Long cityId,
                                                           HttpServletRequest request) {
        cityService.deleteCityByAdmin(cityId);
        ApiResponse<CityDto> response = ApiResponse.<CityDto>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City deleted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }


}
