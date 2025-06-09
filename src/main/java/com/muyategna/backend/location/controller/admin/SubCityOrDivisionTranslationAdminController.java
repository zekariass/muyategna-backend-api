package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.AdminPaginationService;
import com.muyategna.backend.location.assembler.SubCityOrDivisionTranslationDtoAdminAssembler;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationCreateDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationDto;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationUpdateDto;
import com.muyategna.backend.location.entity.CityTranslation;
import com.muyategna.backend.location.service.SubCityOrDivisionTranslationService;
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
@RequestMapping("/api/v1/admin/location/sub-city-or-division-translations")
@Tag(name = "Sub-City Or Division Translation Admin Management", description = "Operations related to sub-city or division translation management")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class SubCityOrDivisionTranslationAdminController {

    private final SubCityOrDivisionTranslationService subCityOrDivisionTranslationService;
    private final SubCityOrDivisionTranslationDtoAdminAssembler subCityOrDivisionDtoAssembler;
    private final PagedResourcesAssembler<SubCityOrDivisionTranslationDto> pagedResourcesAssembler;
    private final AdminPaginationService adminPaginationService;

    @Autowired
    public SubCityOrDivisionTranslationAdminController(SubCityOrDivisionTranslationService subCityOrDivisionTranslationService, SubCityOrDivisionTranslationDtoAdminAssembler subCityOrDivisionDtoAssembler, PagedResourcesAssembler<SubCityOrDivisionTranslationDto> pagedResourcesAssembler, AdminPaginationService adminPaginationService) {
        this.subCityOrDivisionTranslationService = subCityOrDivisionTranslationService;
        this.subCityOrDivisionDtoAssembler = subCityOrDivisionDtoAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.adminPaginationService = adminPaginationService;
    }


    /**
     * Retrieves a paged list of sub-city or division translations for a given subCityOrDivision ID.
     *
     * @param subCityOrDivisionId the ID of the subCityOrDivision
     * @param page                the page number
     * @param size                the number of items per page
     * @param sortBy              the field to sort on
     * @param request             the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the requested SubCityOrDivisionTranslationDto entity as a PagedModel of EntityModel
     */
    @GetMapping("/subCityOrDivision/{subCityOrDivisionId}")
    @Operation(summary = "Get sub-city or division translations by subCityOrDivision ID", description = "Get sub-city or division translations by subCityOrDivision ID")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<SubCityOrDivisionTranslationDto>>>> getSubCityOrDivisionTranslationsBySubCityOrDivisionId(@Parameter(description = "SubCityOrDivision id") @PathVariable Long subCityOrDivisionId,
                                                                                                                                                       @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
                                                                                                                                                       @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
                                                                                                                                                       @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
                                                                                                                                                       HttpServletRequest request) {
        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, CityTranslation.class);
        Page<SubCityOrDivisionTranslationDto> pagedTranslationsDto = subCityOrDivisionTranslationService.getSubCityOrDivisionTranslationsBySubCityOrDivisionId(subCityOrDivisionId, pageable);


        PagedModel<EntityModel<SubCityOrDivisionTranslationDto>> collectionModel = pagedResourcesAssembler.toModel(pagedTranslationsDto,
                dto -> subCityOrDivisionDtoAssembler.toModel(
                        dto,
                        adminPaginationService.getSafePage(page),
                        adminPaginationService.getSafeSize(size),
                        adminPaginationService.getSafeSortBy(sortBy, CityTranslation.class),
                        request));

        ApiResponse<PagedModel<EntityModel<SubCityOrDivisionTranslationDto>>> response = ApiResponse.<PagedModel<EntityModel<SubCityOrDivisionTranslationDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("SubCityOrDivision translations retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a subCityOrDivision translation by its ID.
     *
     * @param translationId the ID of the subCityOrDivision translation to be retrieved
     * @param request       the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested SubCityOrDivisionTranslationDto entity as an EntityModel
     * @throws ResourceNotFoundException if no subCityOrDivision translation is found with the given ID
     */
    @GetMapping("/{translationId}")
    @Operation(summary = "Get subCityOrDivision translations by subCityOrDivision ID", description = "Get subCityOrDivision translations by subCityOrDivision ID")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>>> getSubCityOrDivisionTranslationById(@Parameter(description = "Translation id") @PathVariable Long translationId,
                                                                                                                         HttpServletRequest request) {

        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto = subCityOrDivisionTranslationService.getSubCityOrDivisionTranslationById(translationId);

        EntityModel<SubCityOrDivisionTranslationDto> entityModel = subCityOrDivisionDtoAssembler.toModel(subCityOrDivisionTranslationDto, request);

        ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("SubCityOrDivision translations retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a subCityOrDivision translation by its subCityOrDivision ID and language ID or locale.
     *
     * @param subCityOrDivisionId the ID of the subCityOrDivision
     * @param languageId          the ID of the language, optional
     * @param locale              the locale, optional
     * @param request             the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the requested SubCityOrDivisionTranslationDto entity as an EntityModel
     * @throws IllegalArgumentException if neither languageId nor locale is provided
     */
    @GetMapping
    @Operation(summary = "SubCityOrDivision translation by subCityOrDivisionId and languageId or locale", description = "SubCityOrDivision translation by subCityOrDivisionId and languageId or locale")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>>> getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLanguageIdOrLocale(
            @Parameter(description = "SubCityOrDivision id") @RequestParam Long subCityOrDivisionId,
            @Parameter(description = "Language id") @RequestParam(required = false) Long languageId,
            @Parameter(description = "Locale") @RequestParam(required = false) String locale,
            HttpServletRequest request) {

        SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto;

        if (languageId != null) {
            subCityOrDivisionTranslationDto = subCityOrDivisionTranslationService.getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLanguageId(subCityOrDivisionId, languageId);
        } else if (locale != null) {
            subCityOrDivisionTranslationDto = subCityOrDivisionTranslationService.getSubCityOrDivisionTranslationBySubCityOrDivisionIdAndLocale(subCityOrDivisionId, locale);
        } else {
            throw new IllegalArgumentException("Either languageId or locale must be provided.");
        }

        EntityModel<SubCityOrDivisionTranslationDto> subCityOrDivisionTranslationModel = subCityOrDivisionDtoAssembler.toModel(subCityOrDivisionTranslationDto, request);

        ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("SubCityOrDivision translation fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(subCityOrDivisionTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Updates a subCityOrDivision translation with the specified ID.
     *
     * @param translationId                   the ID of the subCityOrDivision translation to be updated
     * @param subCityOrDivisionTranslationDto the subCityOrDivision translation object with new values
     * @param request                         the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the updated subCityOrDivision translation details
     */
    @PutMapping("/{translationId}")
    @Operation(summary = "Update subCityOrDivision translation", description = "Update subCityOrDivision translation")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>>> updateCityTranslation(@Parameter(description = "SubCityOrDivision translation id") @PathVariable Long translationId,
                                                                                                           @Parameter(description = "SubCityOrDivision translation object") @Valid @RequestBody SubCityOrDivisionTranslationUpdateDto subCityOrDivisionTranslationDto,
                                                                                                           HttpServletRequest request) {
        SubCityOrDivisionTranslationDto updatedCityTranslationDto = subCityOrDivisionTranslationService.updateSubCityOrDivisionTranslation(translationId, subCityOrDivisionTranslationDto);

        EntityModel<SubCityOrDivisionTranslationDto> updatedCityTranslationModel = subCityOrDivisionDtoAssembler.toModel(updatedCityTranslationDto, request);

        ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("SubCityOrDivision translation with id: %d is updated successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(updatedCityTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Creates a new subCityOrDivision translation.
     *
     * @param subCityOrDivisionTranslationDto the subCityOrDivision translation object containing the details of the new translation
     * @param request                         the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the created subCityOrDivision translation details
     */
    @PostMapping
    @Operation(summary = "Create subCityOrDivision translation", description = "Create subCityOrDivision translation")
    public ResponseEntity<ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>>> AddNewCityTranslation(@Valid @RequestBody SubCityOrDivisionTranslationCreateDto subCityOrDivisionTranslationDto,
                                                                                                           HttpServletRequest request) {
        SubCityOrDivisionTranslationDto createdCityTranslationDto = subCityOrDivisionTranslationService.addNewSubCityOrDivisionTranslation(subCityOrDivisionTranslationDto);

        EntityModel<SubCityOrDivisionTranslationDto> createdCityTranslationModel = subCityOrDivisionDtoAssembler.toModel(createdCityTranslationDto, request);

        ApiResponse<EntityModel<SubCityOrDivisionTranslationDto>> response = ApiResponse.<EntityModel<SubCityOrDivisionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("SubCityOrDivision translation is created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(createdCityTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Deletes a subCityOrDivision translation by its ID.
     *
     * @param translationId the ID of the subCityOrDivision translation to be deleted
     * @param request       the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse indicating the success of the deletion
     */
    @DeleteMapping("/{translationId}")
    @Operation(summary = "Delete subCityOrDivision translation", description = "Delete subCityOrDivision translation")
    public ResponseEntity<ApiResponse<Void>> deleteCityTranslation(@Parameter(description = "SubCityOrDivision translation id") @PathVariable Long translationId,
                                                                   HttpServletRequest request) {
        subCityOrDivisionTranslationService.deleteSubCityOrDivisionTranslation(translationId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("SubCityOrDivision translation with id: %d is deleted successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
