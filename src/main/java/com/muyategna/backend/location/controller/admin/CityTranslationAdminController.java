package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.service.AdminPaginationService;
import com.muyategna.backend.location.assembler.CityTranslationDtoAdminAssembler;
import com.muyategna.backend.location.dto.city.CityTranslationCreateDto;
import com.muyategna.backend.location.dto.city.CityTranslationDto;
import com.muyategna.backend.location.dto.city.CityTranslationUpdateDto;
import com.muyategna.backend.location.entity.CityTranslation;
import com.muyategna.backend.location.service.CityTranslationService;
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
@RequestMapping("/api/v1/admin/location/city-translations")
@Tag(name = "City Translation Admin Management", description = "Operations related to city translation management")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class CityTranslationAdminController {

    private final CityTranslationService cityTranslationService;
    private final CityTranslationDtoAdminAssembler cityTranslationDtoAdminAssembler;
    private final PagedResourcesAssembler<CityTranslationDto> pagedResourcesAssembler;
    private final AdminPaginationService adminPaginationService;

    @Autowired
    public CityTranslationAdminController(CityTranslationService cityTranslationService, CityTranslationDtoAdminAssembler cityTranslationDtoAdminAssembler, PagedResourcesAssembler<CityTranslationDto> pagedResourcesAssembler, AdminPaginationService adminPaginationService) {
        this.cityTranslationService = cityTranslationService;
        this.cityTranslationDtoAdminAssembler = cityTranslationDtoAdminAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.adminPaginationService = adminPaginationService;
    }


    /**
     * Gets city translations by city ID.
     *
     * @param cityId  City ID to filter on
     * @param page    Page number to retrieve (default is 1)
     * @param size    Page size to retrieve (default is 10)
     * @param sortBy  Sort field (default is "name")
     * @param request {@link HttpServletRequest} to get the current request
     * @return A paged list of city translations in the specified page
     */
    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get city translations by city ID", description = "Get city translations by city ID")
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<CityTranslationDto>>>> getCityTranslationsByCityId(@Parameter(description = "City id") @PathVariable Long cityId,
                                                                                                                @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer page,
                                                                                                                @Parameter(description = "Page size") @RequestParam(required = false) Integer size,
                                                                                                                @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
                                                                                                                HttpServletRequest request) {
        Pageable pageable = adminPaginationService.getPagination(page, size, sortBy, CityTranslation.class);
        Page<CityTranslationDto> pagedCityTranslationDto = cityTranslationService.getCityTranslationsByCityId(cityId, pageable);


        PagedModel<EntityModel<CityTranslationDto>> collectionModel = pagedResourcesAssembler.toModel(pagedCityTranslationDto,
                dto -> cityTranslationDtoAdminAssembler.toModel(
                        dto,
                        adminPaginationService.getSafePage(page),
                        adminPaginationService.getSafeSize(size),
                        adminPaginationService.getSafeSortBy(sortBy, CityTranslation.class),
                        request));

        ApiResponse<PagedModel<EntityModel<CityTranslationDto>>> response = ApiResponse.<PagedModel<EntityModel<CityTranslationDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City translations retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a city translation by its translation ID.
     *
     * @param translationId the ID of the city translation to be retrieved
     * @param request       the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the city translation details
     * @throws ResourceNotFoundException if the city translation is not found for the given ID
     */
    @GetMapping("/{translationId}")
    @Operation(summary = "Get city translations by city ID", description = "Get city translations by city ID")
    public ResponseEntity<ApiResponse<EntityModel<CityTranslationDto>>> getCityTranslationsById(@Parameter(description = "Translation id") @PathVariable Long translationId,
                                                                                                HttpServletRequest request) {

        CityTranslationDto cityTranslationDto = cityTranslationService.getCityTranslationById(translationId);

        EntityModel<CityTranslationDto> entityModel = cityTranslationDtoAdminAssembler.toModel(cityTranslationDto, request);

        ApiResponse<EntityModel<CityTranslationDto>> response = ApiResponse.<EntityModel<CityTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City translations retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a city translation by its city ID and language ID or locale.
     *
     * @param cityId     the ID of the city to be retrieved
     * @param languageId the ID of the language to filter on (optional)
     * @param locale     the locale to filter on (optional)
     * @param request    the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the city translation details
     * @throws IllegalArgumentException if neither languageId nor locale is provided
     */
    @GetMapping
    @Operation(summary = "City translation by city id and language id or locale", description = "City translation by city id and language id or locale")
    public ResponseEntity<ApiResponse<EntityModel<CityTranslationDto>>> getCityTranslationByCityIdAndLanguageIdOrLocale(
            @Parameter(description = "City id") @RequestParam Long cityId,
            @Parameter(description = "Language id") @RequestParam(required = false) Long languageId,
            @Parameter(description = "Locale") @RequestParam(required = false) String locale,
            HttpServletRequest request) {

        CityTranslationDto cityTranslationDto;

        if (languageId != null) {
            cityTranslationDto = cityTranslationService.getCityTranslationByCityIdAndLanguageId(cityId, languageId);
        } else if (locale != null) {
            cityTranslationDto = cityTranslationService.getCityTranslationByCityIdAndLocale(cityId, locale);
        } else {
            throw new IllegalArgumentException("Either languageId or locale must be provided.");
        }

        EntityModel<CityTranslationDto> cityTranslationModel = cityTranslationDtoAdminAssembler.toModel(cityTranslationDto, request);

        ApiResponse<EntityModel<CityTranslationDto>> response = ApiResponse.<EntityModel<CityTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City translation fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(cityTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Updates a city translation with the specified ID.
     *
     * @param translationId      the ID of the city translation to be updated
     * @param cityTranslationDto the city translation object with new values
     * @param request            the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the updated city translation details
     */
    @PutMapping("/{translationId}")
    @Operation(summary = "Update city translation", description = "Update city translation")
    public ResponseEntity<ApiResponse<EntityModel<CityTranslationDto>>> updateCityTranslation(@Parameter(description = "City translation id") @PathVariable Long translationId,
                                                                                              @Parameter(description = "City translation object") @Valid @RequestBody CityTranslationUpdateDto cityTranslationDto,
                                                                                              HttpServletRequest request) {
        CityTranslationDto updatedCityTranslationDto = cityTranslationService.updateCityTranslation(translationId, cityTranslationDto);

        EntityModel<CityTranslationDto> updatedCityTranslationModel = cityTranslationDtoAdminAssembler.toModel(updatedCityTranslationDto, request);

        ApiResponse<EntityModel<CityTranslationDto>> response = ApiResponse.<EntityModel<CityTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City translation with id: %d is updated successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(updatedCityTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Creates a new city translation.
     *
     * @param cityTranslationDto the city translation object containing the details of the new translation
     * @param request            the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse with the created city translation details
     */
    @PostMapping
    @Operation(summary = "Create city translation", description = "Create city translation")
    public ResponseEntity<ApiResponse<EntityModel<CityTranslationDto>>> AddNewCityTranslation(@Valid @RequestBody CityTranslationCreateDto cityTranslationDto,
                                                                                              HttpServletRequest request) {
        CityTranslationDto createdCityTranslationDto = cityTranslationService.addNewCityTranslation(cityTranslationDto);

        EntityModel<CityTranslationDto> createdCityTranslationModel = cityTranslationDtoAdminAssembler.toModel(createdCityTranslationDto, request);

        ApiResponse<EntityModel<CityTranslationDto>> response = ApiResponse.<EntityModel<CityTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City translation is created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(createdCityTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Deletes a city translation by its ID.
     *
     * @param translationId the ID of the city translation to be deleted
     * @param request       the HTTP servlet request
     * @return a ResponseEntity containing an ApiResponse indicating the success of the deletion
     */
    @DeleteMapping("/{translationId}")
    @Operation(summary = "Delete city translation", description = "Delete city translation")
    public ResponseEntity<ApiResponse<Void>> deleteCityTranslation(@Parameter(description = "City translation id") @PathVariable Long translationId,
                                                                   HttpServletRequest request) {
        cityTranslationService.deleteCityTranslation(translationId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("City translation with id: %d is deleted successfully".formatted(translationId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
