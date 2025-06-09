package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.location.assembler.RegionTranslationDtoAdminAssembler;
import com.muyategna.backend.location.dto.region.RegionTranslationCreateDto;
import com.muyategna.backend.location.dto.region.RegionTranslationDto;
import com.muyategna.backend.location.dto.region.RegionTranslationUpdateDto;
import com.muyategna.backend.location.service.RegionTranslationService;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/location/region-translations")
@Tag(name = "Region Translation Admin Management", description = "Operations related to region translation management")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class RegionTranslationAdminController {
    private final RegionTranslationService regionTranslationService;
    private final RegionTranslationDtoAdminAssembler regionTranslationDtoAdminAssembler;

    public RegionTranslationAdminController(RegionTranslationService regionTranslationService, RegionTranslationDtoAdminAssembler regionTranslationDtoAdminAssembler) {
        this.regionTranslationService = regionTranslationService;
        this.regionTranslationDtoAdminAssembler = regionTranslationDtoAdminAssembler;
    }


    /**
     * Retrieves all region translations for a given region ID.
     *
     * @param regionId the ID of the region for which translations are to be retrieved
     * @param request  the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with a CollectionModel of EntityModel<RegionTranslationDto> objects
     * @throws ResourceNotFoundException if no region translations are found for the given region ID
     */
    @GetMapping("/region/{regionId}")
    @Operation(summary = "Get region translations by region ID", description = "Get region translations by region ID")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<RegionTranslationDto>>>> getRegionTranslationsByRegionId(@Parameter(description = "Region id") @PathVariable Long regionId,
                                                                                                                           HttpServletRequest request) {
        List<RegionTranslationDto> regionTranslations = regionTranslationService.getRegionTranslationsByRegionId(regionId);
        CollectionModel<EntityModel<RegionTranslationDto>> regionTranslationsModel = regionTranslationDtoAdminAssembler.toCollectionModel(regionTranslations, request);
        ApiResponse<CollectionModel<EntityModel<RegionTranslationDto>>> response = ApiResponse.<CollectionModel<EntityModel<RegionTranslationDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region translations for region with id: %d are retrieved successfully".formatted(regionId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regionTranslationsModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a region translation by its ID.
     *
     * @param translationId the ID of the region translation to be retrieved
     * @param request       the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested RegionTranslationDto entity as an EntityModel
     * @throws ResourceNotFoundException if no region translation is found with the given ID
     */
    @GetMapping("/{translationId}")
    @Operation(summary = "Get region translation by id", description = "Get region translation by id")
    public ResponseEntity<ApiResponse<EntityModel<RegionTranslationDto>>> getRegionTranslationById(@Parameter(description = "Region translation id") @PathVariable Long translationId,
                                                                                                   HttpServletRequest request) {
        RegionTranslationDto regionTranslationDto = regionTranslationService.getRegionTranslationById(translationId);
        EntityModel<RegionTranslationDto> regionTranslationModel = regionTranslationDtoAdminAssembler.toModel(regionTranslationDto, request);
        ApiResponse<EntityModel<RegionTranslationDto>> response = ApiResponse.<EntityModel<RegionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region translation with id: %d is retrieved successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regionTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a region translation by its region ID and language ID or locale.
     *
     * @param regionId   the ID of the region
     * @param locale     the locale, optional
     * @param languageId the ID of the language, optional
     * @param request    the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested RegionTranslationDto entity as an EntityModel
     * @throws IllegalArgumentException if neither languageId nor locale is provided
     */
    @GetMapping
    @Operation(summary = "Get region translation by region ID and language ID or locale", description = "Get all region translations")
    public ResponseEntity<ApiResponse<EntityModel<RegionTranslationDto>>> getRegionTranslationByRegionIdAndLanguageIdOrLocale(
            @Parameter(description = "Region id") @RequestParam Long regionId,
            @Parameter(description = "Locale", required = false) @RequestParam(required = false) String locale,
            @Parameter(description = "Language id", required = false) @RequestParam(required = false) Long languageId,
            HttpServletRequest request) {

        RegionTranslationDto regionTranslationDto;

        if (languageId != null) {
            regionTranslationDto = regionTranslationService.getRegionTranslationByRegionIdAndLanguageId(regionId, languageId);
        } else if (locale != null) {
            regionTranslationDto = regionTranslationService.getRegionTranslationByRegionIdAndLocale(regionId, locale);
        } else {
            throw new IllegalArgumentException("Either languageId or locale must be provided.");
        }

        EntityModel<RegionTranslationDto> regionTranslationModel = regionTranslationDtoAdminAssembler.toModel(regionTranslationDto, request);

        ApiResponse<EntityModel<RegionTranslationDto>> response = ApiResponse.<EntityModel<RegionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region translation fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(regionTranslationModel)
                .build();
        return ResponseEntity.ok(response);

    }


    /**
     * Updates a region translation with the given id.
     *
     * <p>If the given id does not exist, this method returns a 404 error.
     *
     * @param translationId        the id of the region translation to be updated
     * @param regionTranslationDto the region translation object containing the updated fields
     * @param request              the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the updated RegionTranslationDto entity as an EntityModel
     * @throws ResourceNotFoundException if the given id does not exist
     */
    @PutMapping("/{translationId}")
    @Operation(summary = "Update region translation", description = "Update region translation")
    public ResponseEntity<ApiResponse<EntityModel<RegionTranslationDto>>> updateRegionTranslation(@Parameter(description = "Region translation id") @PathVariable Long translationId,
                                                                                                  @Parameter(description = "Region translation object") @Valid @RequestBody RegionTranslationUpdateDto regionTranslationDto,
                                                                                                  HttpServletRequest request) {
        RegionTranslationDto updatedRegionTranslationDto = regionTranslationService.updateRegionTranslation(translationId, regionTranslationDto);

        EntityModel<RegionTranslationDto> updatedRegionTranslationModel = regionTranslationDtoAdminAssembler.toModel(updatedRegionTranslationDto, request);

        ApiResponse<EntityModel<RegionTranslationDto>> response = ApiResponse.<EntityModel<RegionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region translation with id: %d is updated successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(updatedRegionTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Creates a new region translation.
     *
     * @param regionTranslationDto the region translation object containing the details of the new translation
     * @param request              the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the created region translation details
     */
    @PostMapping
    @Operation(summary = "Create region translation", description = "Create region translation")
    public ResponseEntity<ApiResponse<EntityModel<RegionTranslationDto>>> AddNewRegionTranslation(@Valid @RequestBody RegionTranslationCreateDto regionTranslationDto,
                                                                                                  HttpServletRequest request) {
        RegionTranslationDto createdRegionTranslationDto = regionTranslationService.addNewRegionTranslation(regionTranslationDto);

        EntityModel<RegionTranslationDto> createdRegionTranslationModel = regionTranslationDtoAdminAssembler.toModel(createdRegionTranslationDto, request);

        ApiResponse<EntityModel<RegionTranslationDto>> response = ApiResponse.<EntityModel<RegionTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region translation is created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(createdRegionTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Deletes a region translation by its ID.
     *
     * @param translationId the ID of the region translation to be deleted
     * @param request       the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse indicating the deletion status
     * @throws ResourceNotFoundException if no region translation is found with the given ID
     */
    @DeleteMapping("/{translationId}")
    @Operation(summary = "Delete region translation", description = "Delete region translation")
    public ResponseEntity<ApiResponse<Void>> deleteRegionTranslation(@Parameter(description = "Region translation id") @PathVariable Long translationId,
                                                                     HttpServletRequest request) {
        regionTranslationService.deleteRegionTranslation(translationId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Region translation with id: %d is deleted successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

}
