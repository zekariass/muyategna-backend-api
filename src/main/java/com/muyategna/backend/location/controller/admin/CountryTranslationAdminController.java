package com.muyategna.backend.location.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.location.assembler.CountryTranslationDtoAdminAssembler;
import com.muyategna.backend.location.dto.country.CountryTranslationCreateDto;
import com.muyategna.backend.location.dto.country.CountryTranslationDto;
import com.muyategna.backend.location.dto.country.CountryTranslationUpdateDto;
import com.muyategna.backend.location.service.CountryTranslationService;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/location/country-translations")
@Tag(name = "Country Translation Admin Management", description = "Operations related to country translation management")
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
public class CountryTranslationAdminController {
    private final CountryTranslationService countryTranslationService;
    private final CountryTranslationDtoAdminAssembler countryTranslationDtoAdminAssembler;

    @Autowired
    public CountryTranslationAdminController(CountryTranslationService countryTranslationService, CountryTranslationDtoAdminAssembler countryTranslationDtoAdminAssembler) {
        this.countryTranslationService = countryTranslationService;
        this.countryTranslationDtoAdminAssembler = countryTranslationDtoAdminAssembler;
    }


    /**
     * Returns a list of translations for a given country id.
     *
     * @param countryId the id of the country
     * @param request   the HttpServletRequest
     * @return a ResponseEntity with a CollectionModel of CountryTranslationDto
     * @throws ResourceNotFoundException if no translations are found for the given country id
     */
    @GetMapping("/country/{countryId}")
    @Operation(summary = "Get all country translations", description = "Get all country translations")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<CountryTranslationDto>>>> getAllCountryTranslations(
            @Parameter(description = "Country id") @PathVariable Long countryId,
            HttpServletRequest request
    ) {
        List<CountryTranslationDto> countryTranslationsDto = countryTranslationService.getCountryTranslationsByCountryId(countryId);

        if (countryTranslationsDto.isEmpty()) {
            throw new ResourceNotFoundException("No translations found for country id: " + countryId);
        }

        CollectionModel<EntityModel<CountryTranslationDto>> countryTranslationsModel = countryTranslationDtoAdminAssembler.toCollectionModel(countryTranslationsDto, request);

        ApiResponse<CollectionModel<EntityModel<CountryTranslationDto>>> response = ApiResponse.<CollectionModel<EntityModel<CountryTranslationDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country translations fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryTranslationsModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a country translation by its ID.
     *
     * @param translationId the ID of the country translation to be retrieved
     * @param request       the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested CountryTranslationDto entity as an EntityModel
     * @throws ResourceNotFoundException if no country translation is found with the given ID
     */
    @GetMapping("/{translationId}")
    @Operation(summary = "Get country translation by id", description = "Get country translation by id")
    public ResponseEntity<ApiResponse<EntityModel<CountryTranslationDto>>> getCountryTranslationById(
            @Parameter(description = "Country translation id", required = true) @PathVariable Long translationId,
            HttpServletRequest request
    ) {
        CountryTranslationDto countryTranslationDto = countryTranslationService.getCountryTranslationById(translationId);

        EntityModel<CountryTranslationDto> countryTranslationModel = countryTranslationDtoAdminAssembler.toModel(countryTranslationDto, request);

        ApiResponse<EntityModel<CountryTranslationDto>> response = ApiResponse.<EntityModel<CountryTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country translation fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves a country translation by its country id and language id or locale.
     *
     * @param countryId  the id of the country
     * @param languageId the id of the language, optional
     * @param locale     the locale, optional
     * @param request    the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the requested CountryTranslationDto entity as an EntityModel
     * @throws IllegalArgumentException if neither languageId nor locale is provided
     */
    @GetMapping
    @Operation(summary = "Get country translation by country id and language id", description = "Get country translation by country id and language id")
    public ResponseEntity<ApiResponse<EntityModel<CountryTranslationDto>>> getCountryTranslationByCountryIdAndLanguageIdOrLocale(
            @Parameter(description = "Country id", required = true) @RequestParam Long countryId,
            @Parameter(description = "Language id", required = false) @RequestParam(required = false) Long languageId,
            @Parameter(description = "Locale", required = false) @RequestParam(required = false) String locale,
            HttpServletRequest request
    ) {

        CountryTranslationDto countryTranslationDto;

        if (languageId != null) {
            countryTranslationDto = countryTranslationService.getCountryTranslationByCountryIdAndLanguageId(countryId, languageId);
        } else if (locale != null) {
            countryTranslationDto = countryTranslationService.getCountryTranslationByCountryIdAndLocale(countryId, locale);
        } else {
            throw new IllegalArgumentException("Either languageId or locale must be provided.");
        }

        EntityModel<CountryTranslationDto> countryTranslationModel = countryTranslationDtoAdminAssembler.toModel(countryTranslationDto, request);

        ApiResponse<EntityModel<CountryTranslationDto>> response = ApiResponse.<EntityModel<CountryTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country translation fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{translationId}")
    @Operation(summary = "Update country translation", description = "Update country translation")
    public ResponseEntity<ApiResponse<EntityModel<CountryTranslationDto>>> updateCountryTranslation(
            @Parameter(description = "Country translation id") @PathVariable Long translationId,
            @Parameter(description = "Country translation object") @Valid @RequestBody CountryTranslationUpdateDto countryTranslationDto,
            HttpServletRequest request
    ) {
        CountryTranslationDto updatedCountryTranslationDto = countryTranslationService.updateCountryTranslation(translationId, countryTranslationDto);
        EntityModel<CountryTranslationDto> countryTranslationModel = countryTranslationDtoAdminAssembler.toModel(updatedCountryTranslationDto, request);
        ApiResponse<EntityModel<CountryTranslationDto>> response = ApiResponse.<EntityModel<CountryTranslationDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country translation updated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryTranslationModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Handles HTTP POST request for creating a new country translation.
     *
     * @param countryTranslationDto the data transfer object containing the details of the country translation to be created
     * @param request               the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse with the created CountryTranslationDto as an EntityModel
     * The response includes the details of the newly created country translation
     */
    @PostMapping
    @Operation(summary = "Create country translation", description = "Create country translation")
    public ResponseEntity<ApiResponse<EntityModel<CountryTranslationDto>>> createCountryTranslation(
            @Parameter(description = "Country translation object") @Valid @RequestBody CountryTranslationCreateDto countryTranslationDto,
            HttpServletRequest request
    ) {
        CountryTranslationDto createdCountryTranslationDto = countryTranslationService.addNewCountryTranslation(countryTranslationDto);
        EntityModel<CountryTranslationDto> countryTranslationModel = countryTranslationDtoAdminAssembler.toModel(createdCountryTranslationDto, request);
        ApiResponse<EntityModel<CountryTranslationDto>> response = ApiResponse.<EntityModel<CountryTranslationDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Country translation created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(countryTranslationModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Deletes a country translation with the specified ID.
     *
     * @param translationId the ID of the country translation to be deleted
     * @param request       the HttpServletRequest object containing request details
     * @return a ResponseEntity containing an ApiResponse indicating the success of the deletion operation
     * The response includes a success message if the deletion is successful
     * @throws ResourceNotFoundException if no country translation is found with the given ID
     */
    @DeleteMapping("/{translationId}")
    @Operation(summary = "Delete country translation", description = "Delete country translation")
    public ResponseEntity<ApiResponse<Void>> deleteCountryTranslation(
            @Parameter(description = "Country translation id") @PathVariable Long translationId,
            HttpServletRequest request
    ) {
        countryTranslationService.deleteCountryTranslation(translationId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Country translation deleted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
