package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.AdminPaginationService;
import com.muyategna.backend.location.assembler.SubCityOrDivisionDtoModelAdminAssembler;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionCreateDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionUpdateDto;
import com.muyategna.backend.location.entity.SubCityOrDivision;
import com.muyategna.backend.location.service.SubCityOrDivisionService;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
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
@RequestMapping("/api/v1/admin/location/sub-cities-or-divisions")
@Tag(name = "Sub-City or Division Admin Management", description = "Admin endpoints for sub-cities or divisions")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class SubCityOrDivisionAdminController {
    private final SubCityOrDivisionService subCityOrDivisionService;
    private final SubCityOrDivisionDtoModelAdminAssembler subCityOrDivisionDtoAssembler;
    private final PagedResourcesAssembler<SubCityOrDivisionDto> pagedResourcesAssembler;
    private final AdminPaginationService adminPaginationService;

    @Autowired
    public SubCityOrDivisionAdminController(SubCityOrDivisionService subCityOrDivisionService, SubCityOrDivisionDtoModelAdminAssembler subCityOrDivisionDtoAssembler, PagedResourcesAssembler<SubCityOrDivisionDto> pagedResourcesAssembler, AdminPaginationService adminPaginationService) {
        this.subCityOrDivisionService = subCityOrDivisionService;
        this.subCityOrDivisionDtoAssembler = subCityOrDivisionDtoAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.adminPaginationService = adminPaginationService;
    }


    /**
     * Retrieves a sub-city or division by its ID, translated to the current language.
     *
     * @param subCityOrDivisionId the ID of the sub-city or division to be retrieved
     * @param request             the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the retrieved sub-city or division as an EntityModel
     */
    @GetMapping("/{subCityOrDivisionId}")
    @Operation(summary = "Get sub-city or division by id", description = "Get sub-city or division by id translated to the current language")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionDto>>> getSubCityOrDivisionById(@Parameter(description = "Id of the sub-city or division to be retrieved", required = true) @PathVariable Long subCityOrDivisionId,
                                                                                                   HttpServletRequest request) {
        SubCityOrDivisionDto subCityOrDivisionDto = subCityOrDivisionService.getSubCityOrDivisionById(subCityOrDivisionId);
        EntityModel<SubCityOrDivisionDto> entityModel = subCityOrDivisionDtoAssembler.toModel(subCityOrDivisionDto, request);

        ApiResponse<EntityModel<SubCityOrDivisionDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(subCityOrDivisionDto == null ? "Sub-city or division not found" : "Sub-city or division fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a paginated list of sub-cities or divisions for a given city ID.
     *
     * @param cityId  The ID of the city for which sub-cities or divisions are to be retrieved.
     * @param page    The page number to retrieve, default is 1.
     * @param size    The number of records per page, if not specified, the default size will be used.
     * @param sortBy  The field by which the results should be sorted, default is 'id'.
     * @param request The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with a PagedModel of EntityModel objects.
     * @throws ResourceNotFoundException if no sub-cities or divisions are found for the given city ID.
     */
    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get sub-city or division by city id", description = "Get sub-city or division by city id translated to the current language")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<SubCityOrDivisionDto>>>> getSubCityOrDivisionByCityId(@Parameter(description = "Id of the related city for the sub-city or division to be retrieved", required = true) @PathVariable Long cityId,
                                                                                                                   @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
                                                                                                                   @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
                                                                                                                   @Parameter(description = "Property to sort by") @RequestParam(defaultValue = "id") String sortBy,
                                                                                                                   HttpServletRequest request) {
        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, SubCityOrDivisionDto.class);
        Page<SubCityOrDivisionDto> pagedSubCityOrDivisionDtoList = subCityOrDivisionService.getSubCityOrDivisionByCityId(cityId, pageable);
        if (pagedSubCityOrDivisionDtoList.isEmpty()) {
            throw new ResourceNotFoundException("No sub-cities or divisions found for the city with id " + cityId);
        }
        PagedModel<EntityModel<SubCityOrDivisionDto>> pagedModel = pagedResourcesAssembler.toModel(pagedSubCityOrDivisionDtoList,
                (subCityOrDivisionDto) -> subCityOrDivisionDtoAssembler.toModel(subCityOrDivisionDto, page, size, sortBy, request));


        ApiResponse<PagedModel<EntityModel<SubCityOrDivisionDto>>> response = ApiResponse.<PagedModel<EntityModel<SubCityOrDivisionDto>>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Sub-cities or divisions fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves all sub-cities or divisions translated to the current language.
     *
     * @param page    the page number (starts from 1)
     * @param size    the page size, optional
     * @param sortBy  the property to sort by, optional, default is "id"
     * @param request the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a PagedModel of EntityModel<SubCityOrDivisionDto> objects
     * @throws ResourceNotFoundException if no sub-cities or divisions are found
     */
    @GetMapping
    @Operation(summary = "Get all sub-cities or divisions", description = "Get all sub-cities or divisions translated to the current language")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<SubCityOrDivisionDto>>>> getAllSubCityOrDivisions(@Parameter(description = "Page number", required = false) @RequestParam(defaultValue = "1") Integer page,
                                                                                                               @Parameter(description = "Page size", required = false) @RequestParam(required = false) Integer size,
                                                                                                               @Parameter(description = "Property to sort by", required = false) @RequestParam(defaultValue = "id") String sortBy,
                                                                                                               HttpServletRequest request) {

        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, SubCityOrDivisionDto.class);
        Page<SubCityOrDivisionDto> pagedSubCityOrDivisionDtoList = subCityOrDivisionService.getAllSubCityOrDivisions(pageable);
        if (pagedSubCityOrDivisionDtoList.isEmpty()) {
            throw new ResourceNotFoundException("No sub-cities or divisions found");
        }
        PagedModel<EntityModel<SubCityOrDivisionDto>> pagedModel = pagedResourcesAssembler.toModel(pagedSubCityOrDivisionDtoList,
                (subCityOrDivisionDto) -> subCityOrDivisionDtoAssembler.toModel(subCityOrDivisionDto,
                        adminPaginationService.getSafePage(page),
                        adminPaginationService.getSafeSize(size),
                        adminPaginationService.getSafeSortBy(sortBy, SubCityOrDivision.class),
                        request));


        ApiResponse<PagedModel<EntityModel<SubCityOrDivisionDto>>> response = ApiResponse.<PagedModel<EntityModel<SubCityOrDivisionDto>>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Sub-cities or divisions fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(pagedModel)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Updates a sub-city or division with the specified ID, translated to the current language.
     *
     * @param subCityOrDivisionId  The ID of the sub-city or division to be updated.
     * @param subCityOrDivisionDto The sub-city or division object with new values.
     * @param request              The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with the updated sub-city or division details.
     */
    @PutMapping("/{subCityOrDivisionId}")
    @Operation(summary = "Update sub-city or division", description = "Update sub-city or division translated to the current language")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionDto>>> updateSubCityOrDivision(@Parameter(description = "Id of the sub-city or division to be updated", required = true) @PathVariable Long subCityOrDivisionId,
                                                                                                  @Parameter(description = "Sub-city or division to be updated", required = true) @Valid @RequestBody SubCityOrDivisionUpdateDto subCityOrDivisionDto,
                                                                                                  HttpServletRequest request) {
        SubCityOrDivisionDto updatedSubCityOrDivisionDto = subCityOrDivisionService.updateSubCityOrDivisionByAdmin(subCityOrDivisionId, subCityOrDivisionDto);
        EntityModel<SubCityOrDivisionDto> entityModel = subCityOrDivisionDtoAssembler.toModel(updatedSubCityOrDivisionDto, null);

        ApiResponse<EntityModel<SubCityOrDivisionDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionDto>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Sub-city or division updated successfully")
                .path(null)
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();

        return ResponseEntity.ok(response);
    }


    /**
     * Adds a new sub-city or division, translating to the current language.
     *
     * @param subCityOrDivisionDto The sub-city or division data to be added.
     * @param request              The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse with the created sub-city or division details.
     */
    @PostMapping
    @Operation(summary = "Add new sub-city or division", description = "Add new sub-city or division translated to the current language")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionDto>>> addNewSubCityOrDivision(@Parameter(description = "Sub-city or division to be added", required = true) @Valid @RequestBody SubCityOrDivisionCreateDto subCityOrDivisionDto,
                                                                                                  HttpServletRequest request) {
        SubCityOrDivisionDto updatedSubCityOrDivisionDto = subCityOrDivisionService.addNewSubCityOrDivisionByAdmin(subCityOrDivisionDto);
        EntityModel<SubCityOrDivisionDto> entityModel = subCityOrDivisionDtoAssembler.toModel(updatedSubCityOrDivisionDto, null);

        ApiResponse<EntityModel<SubCityOrDivisionDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionDto>>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Sub-city or division updated successfully")
                .path(null)
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Deletes a sub-city or division by its ID.
     *
     * @param subCityOrDivisionId The ID of the sub-city or division to be deleted.
     * @param request             The HTTP request object.
     * @return A ResponseEntity containing an ApiResponse indicating the success of the operation.
     */
    @DeleteMapping("/{subCityOrDivisionId}")
    @Operation(summary = "Delete sub-city or division", description = "Delete sub-city or division by id. This operation is only for admin")
    public ResponseEntity<ApiResponse<Void>> deleteSubCityOrDivision(@Parameter(description = "Id of the sub-city or division to be deleted", required = true) @PathVariable Long subCityOrDivisionId,
                                                                     HttpServletRequest request) {
        subCityOrDivisionService.deleteSubCityOrDivisionByAdmin(subCityOrDivisionId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Sub-city or division deleted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
