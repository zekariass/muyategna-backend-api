package com.muyategna.backend.common.controller.admin;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.common.assembler.LanguageDtoAdminAssembler;
import com.muyategna.backend.common.dto.language.LanguageCreateDto;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.dto.language.LanguageUpdateDto;
import com.muyategna.backend.common.service.LanguageService;
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
@RequestMapping("/api/v1/admin/common/languages")
@Tag(name = "Language Admin Management", description = "Admin endpoints for language management. Only accessible to admin users. Requires authentication and ADMIN role.")
@PreAuthorize("isAuthenticated() && hasAuthority('ROLE_ADMIN')")
public class LanguageAdminController {

    private final LanguageService languageService;
    private final LanguageDtoAdminAssembler languageDtoAssembler;

    @Autowired
    public LanguageAdminController(LanguageService languageService, LanguageDtoAdminAssembler languageDtoAssembler) {
        this.languageService = languageService;
        this.languageDtoAssembler = languageDtoAssembler;
    }

    /**
     * Get all languages for admin users.
     *
     * @param request the HTTP request
     * @return a response entity containing a collection of language DTOs
     */
    @GetMapping
    @Operation(summary = "Get all languages for admin", description = "Returns a list of all languages for admin users. Requires authentication and ADMIN role.")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<LanguageDto>>>> getAllLanguages(HttpServletRequest request) {
        List<LanguageDto> languagesDto = languageService.getAllLanguagesForAdmin();

        CollectionModel<EntityModel<LanguageDto>> languagesModel = languageDtoAssembler.toCollectionModel(languagesDto, request);

        ApiResponse<CollectionModel<EntityModel<LanguageDto>>> response = ApiResponse.<CollectionModel<EntityModel<LanguageDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Languages fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(languagesModel)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a language by its ID for admin users.
     *
     * @param languageId the ID of the language to fetch
     * @param request    the HTTP request
     * @return a response entity containing the language DTO
     */
    @GetMapping("/{languageId}")
    @Operation(summary = "Get language by ID by admin", description = "Returns a language by its ID for admin users. Requires authentication and ADMIN role.")
    public ResponseEntity<ApiResponse<EntityModel<LanguageDto>>> getLanguageById(Long languageId, HttpServletRequest request) {
        LanguageDto languageDto = languageService.getLanguageById(languageId);

        EntityModel<LanguageDto> languageModel = languageDtoAssembler.toModel(languageDto, request);

        ApiResponse<EntityModel<LanguageDto>> response = ApiResponse.<EntityModel<LanguageDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Language fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(languageModel)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Get languages by country ID for admin users.
     *
     * @param countryId the ID of the country to fetch languages for
     * @param request   the HTTP request
     * @return a response entity containing a collection of language DTOs
     */
    @GetMapping("/country/{countryId}")
    @Operation(summary = "Get languages by country ID by admin", description = "Returns a list of languages by country ID for admin users. Requires authentication and ADMIN role.")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<LanguageDto>>>> getLanguagesByCountryId(@Parameter(description = "Id of the related country", required = true) @PathVariable Long countryId,
                                                                                                          HttpServletRequest request) {
        List<LanguageDto> languagesDto = languageService.getLanguagesByCountryId(countryId);

        CollectionModel<EntityModel<LanguageDto>> languagesModel = languageDtoAssembler.toCollectionModel(languagesDto, request);

        ApiResponse<CollectionModel<EntityModel<LanguageDto>>> response = ApiResponse.<CollectionModel<EntityModel<LanguageDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Languages fetched successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(languagesModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Create a new language by admin users.
     *
     * @param languageCreateDto the DTO containing the details of the language to create
     * @param request           the HTTP request
     * @return a response entity containing the created language DTO
     */
    @PostMapping
    @Operation(summary = "Create a new language by admin", description = "Creates a new language by admin users. Requires authentication and ADMIN role.")
    public ResponseEntity<ApiResponse<EntityModel<LanguageDto>>> addNewLanguage(@Parameter(description = "Language object", required = true) @Valid @RequestBody LanguageCreateDto languageCreateDto,
                                                                                HttpServletRequest request) {
        LanguageDto createdLanguage = languageService.addNewLanguage(languageCreateDto);

        EntityModel<LanguageDto> languageModel = languageDtoAssembler.toModel(createdLanguage, request);

        ApiResponse<EntityModel<LanguageDto>> response = ApiResponse.<EntityModel<LanguageDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Language created successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(languageModel)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Update an existing language by admin users.
     *
     * @param languageId        the ID of the language to update
     * @param languageUpdateDto the DTO containing the updated details of the language
     * @param request           the HTTP request
     * @return a response entity containing the updated language DTO
     */
    @PutMapping("/{languageId}")
    @Operation(summary = "Update a language by admin", description = "Updates a language for admin users. Requires authentication and ADMIN role.")
    public ResponseEntity<ApiResponse<EntityModel<LanguageDto>>> updateLanguage(@Parameter(description = "Id of the language to update", required = true) @PathVariable Long languageId,
                                                                                @Parameter(description = "Language object", required = true) @Valid @RequestBody LanguageUpdateDto languageUpdateDto,
                                                                                HttpServletRequest request) {
        LanguageDto updatedLanguage = languageService.updateLanguage(languageId, languageUpdateDto);

        EntityModel<LanguageDto> languageModel = languageDtoAssembler.toModel(updatedLanguage, request);

        ApiResponse<EntityModel<LanguageDto>> response = ApiResponse.<EntityModel<LanguageDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Language updated successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(languageModel)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * Delete a language by admin users.
     *
     * @param languageId the ID of the language to delete
     * @param request    the HTTP request
     * @return a response entity indicating the result of the deletion
     */
    @DeleteMapping("/{languageId}")
    @Operation(summary = "Delete a language by admin", description = "Deletes a language for admin users. Requires authentication and ADMIN role.")
    public ResponseEntity<ApiResponse<Void>> deleteLanguage(Long languageId, HttpServletRequest request) {
        languageService.deleteLanguage(languageId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Language deleted successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
